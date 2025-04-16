package com.tongji.sportmanagement.ReservationSubsystem.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tongji.sportmanagement.AccountSubsystem.DTO.NotificationContentDTO;
import com.tongji.sportmanagement.AccountSubsystem.DTO.UserInfoDetailDTO;
import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationType;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Controller.ManagementController;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationStateCountDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiOperationType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Service.ApiConfigService;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupService;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.CancelReservationDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.CancelReservationType;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.GroupRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.GroupResponseDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.IndividualRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.IndividualResponseDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.MatchRequestDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.MatchResponseDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationBasicDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationDetailDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationGroupDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationMetaDTO;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationUserDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.GroupReservation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.MatchReservation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationOperation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationRecord;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationUserState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.GroupReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.MatchReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRecordRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.UserReservationRepository;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabilityState;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Service.CourtService;
import com.tongji.sportmanagement.VenueSubsystem.Service.TimeslotService;

@Service
public class ReservationService
{
  @Autowired
  private ReservationRepository reservationRepository;
  @Autowired
  private UserReservationRepository userReservationRepository;
  @Autowired
  private ReservationRecordRepository reservationRecordRepository;
  @Autowired
  private GroupReservationRepository groupReservationRepository;
  @Autowired
  private MatchReservationRepository matchReservationRepository;
  @Autowired
  private ManagementController managementController; // 用于向场地管理方发送与接收预约信息
  @Autowired
  private TimeslotService timeslotService; // 用于获取开放时间信息
  @Autowired
  private CourtService courtService; // 用于获取场地信息
  @Autowired
  private UserService userService; // 用于获取用户信息
  @Autowired
  private GroupService groupService; // 用于获取团体信息
  @Autowired
  private ApiConfigService apiConfigService; // 用于获取场地管理方信息
  @Autowired
  private ViolationService violationService;
  @Autowired
  private TaskScheduler taskScheduler;

  final static int ReservationPageCount = 10;

  // ---------- 工具函数：用于封装预约流程 ----------

  // 确认预约有效性
  private String reservationConfirm(Reservation reservation, List<ReservationUserDTO> users) throws Exception
  {
    Integer venueId = timeslotService.findVenueByAvailabilityId(reservation.getAvailabilityId());
    ApiOperationType confirmType = apiConfigService.getReservationStrategy(venueId, ApiType.reservation);
    switch (confirmType) {
      case auto:
        reservationConfirmAuto(reservation);
        return "";
      case api:
        return reservationConfirmManager(venueId, reservation, users);
      case manual:
        reservationConfirmManual(reservation);
        return "请等待场馆负责人审核预约申请。";
    }
    return "";
  }

  private synchronized void reservationConfirmAuto(Reservation reservation) throws Exception
  {
    List<ReservationStateCountDTO> stateCount = reservationRepository.countConflictReservation(reservation.getAvailabilityId());
    Long resultCount = 0L;
    for (ReservationStateCountDTO stateCountItem : stateCount) {
      if(stateCountItem.getState() == ReservationState.matching || stateCountItem.getState() == ReservationState.normal){
        resultCount += stateCountItem.getCount();
      }
    }
    if(resultCount > 1){
      throw new ServiceException(422, "存在预约冲突");
    }
    // 添加审核记录
    ReservationRecord validateRecord = new ReservationRecord(null, ReservationOperation.sysvalidate, Instant.now(), null, reservation.getReservationId());
    reservationRecordRepository.save(validateRecord);
  }

  private void reservationConfirmManual(Reservation reservation)
  {
    // 将reservation的状态设置为pending即可
    reservation.setState(ReservationState.pending);
    reservationRepository.save(reservation);
  }

  // 发送请求向场地管理方确认预约
  private String reservationConfirmManager(Integer venueId, Reservation reservationData, List<ReservationUserDTO> reservationUsers) throws Exception
  {
    String reservationRequest = apiConfigService.generateReservationData(venueId, reservationData, reservationUsers);
    String reservationUrl = apiConfigService.getVenueUrl(venueId, ApiType.reservation);
    String reservationResponse = managementController.sendReservationRequest(reservationUrl, reservationRequest);
    Map<String, String> parsedResponse = apiConfigService.parseResponse(venueId, reservationResponse, ApiType.reservation);
    String reservationStatus = parsedResponse.get("status");
    if(reservationStatus.equals("1") || reservationStatus.equals("\"1\"")){
      // 添加审核记录
      ReservationRecord validateRecord = new ReservationRecord(null, ReservationOperation.validate, Instant.now(), null, reservationData.getReservationId());
      reservationRecordRepository.save(validateRecord);
      String successMsg = parsedResponse.get("success_msg");
      if(successMsg != null && successMsg.length() > 2 && successMsg.charAt(0) == '"' && successMsg.charAt(successMsg.length() - 1) == '"'){
        return successMsg.substring(1, successMsg.length() - 1);
      }
      return null;
    }
    else if(reservationStatus.equals("0") || reservationStatus.equals("\"0\"")){
      String msg = "场地管理方拒绝预约请求";
      String additionalMsg = parsedResponse.get("success_msg");
      if(additionalMsg != null && additionalMsg.length() > 2 && additionalMsg.charAt(0) == '"' && additionalMsg.charAt(additionalMsg.length() - 1) == '"'){
        msg += "：" + additionalMsg.substring(1, additionalMsg.length() - 1); 
      }
      throw new ServiceException(409, msg);
    }
    throw new ServiceException(500, "管理员未给出有效的预约结果，请联系场馆负责人");
  }


  // 发送请求向场地管理方申请占用
  private void occupyManagerConfirm(Integer availabilityId) throws Exception
  {
    CourtAvailability courtAvailability = timeslotService.getAvailabilityFullInfo(availabilityId);
    Integer venueId = courtAvailability.getTimeslot().getVenueId();
    ApiOperationType confirmType = apiConfigService.getReservationStrategy(venueId, ApiType.reservation);
    if(confirmType != ApiOperationType.api){
      return;
    }
    String occupyRequest = apiConfigService.generateOccupyData(courtAvailability);
    String occupyUrl = apiConfigService.getVenueUrl(venueId, ApiType.occupy);
    String occupyResponse = managementController.sendReservationRequest(occupyUrl, occupyRequest);
    Map<String, String> parsedResponse = apiConfigService.parseResponse(venueId, occupyResponse, ApiType.occupy);
    String occupyStatus = parsedResponse.get("status");
    if(occupyStatus.equals("1") || occupyStatus.equals("\"1\"")){
      return; // 通过审核
    }
    else if(occupyStatus.equals("0") || occupyStatus.equals("\"0\"")){
      throw new ServiceException(409, "管理员拒绝使用该场地进行拼场预约");
    }
    throw new ServiceException(500, "管理员未给出有效的预约结果，请联系场馆负责人");
  }

  private void scheduleMatchDue(MatchReservation matchReservation)
  {
    taskScheduler.schedule(() -> {
      handleMatchReservationDue(matchReservation.getReservationId());
    }, matchReservation.getExpirationTime());
  }

  private void handleMatchReservationDue(Integer reservationId)
  {
    try{
      Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
      Reservation reservation = reservationOptional.get();
      List<ReservationUserDTO> reservationUsers = userReservationRepository.getUsersByReservationId(reservationId);
      reservationConfirm(reservation, reservationUsers);
      reservation.setState(ReservationState.normal);
      reservationRepository.save(reservation);
      List<UserReservation> userReservations = userReservationRepository.findAllByReservationId(reservationId);
      for (UserReservation userReservation : userReservations) {
        userReservation.setUserState(ReservationUserState.reserved);
      }
      userReservationRepository.saveAll(userReservations);
      CourtAvailability courtAvailability = timeslotService.getAvailability(reservation.getAvailabilityId());
      timeslotService.changeAvailabilityState(courtAvailability, CourtAvailabilityState.full);
    }
    catch(Exception e){
      // e.printStackTrace();
      Optional<Reservation> reservationOptional = reservationRepository.findById(reservationId);
      if(reservationOptional.isEmpty()){
        System.err.println("更新拼场预约时未找到预约项");
        return;
      }
      Reservation reservation = reservationOptional.get();
      reservation.setState(ReservationState.cancelled);
      reservationRepository.save(reservation);
      List<UserReservation> userReservations = userReservationRepository.findAllByReservationId(reservationId);
      for (UserReservation userReservation : userReservations) {
        userReservation.setUserState(ReservationUserState.cancelled);
      }
      userReservationRepository.saveAll(userReservations);
    }
  }

  // 隐去用户姓名和电话，避免前端泄露信息
  private void hideUserInfo(List<ReservationUserDTO> reservationUsers)
  {
    for (ReservationUserDTO reservationUser : reservationUsers){
      String originalPhone = reservationUser.getPhone();
      String originalName = reservationUser.getRealName();
      String replacedName = originalName.substring(0, 1);
      for(int i = 1; i < originalName.length() - 1; i++){
        replacedName += "*";
      }
      replacedName += originalName.charAt(originalName.length() - 1);
      reservationUser.setPhone(originalPhone.substring(0, 3) + "****" + originalPhone.substring(8));
      reservationUser.setRealName(replacedName);
    }
  }

  // 将用户预约信息加入数据库中
  private List<ReservationUserDTO> addReservationUsers(Integer reservationId, List<Integer> users, ReservationUserState state, ReservationOperation operation)
  {
    Instant reservationTime = Instant.now();
    List<UserReservation> userReservations = users.stream().map(user -> new UserReservation(
      null, user, state, reservationId
    )).toList();
    List<ReservationRecord> reservationRecords = users.stream().map(user -> new ReservationRecord(
      null, operation, reservationTime, user, reservationId
    )).toList();
    userReservationRepository.saveAll(userReservations);
    reservationRecordRepository.saveAll(reservationRecords);
    return userReservationRepository.getReservationUsers(userReservations.stream().map(ur -> ur.getUserReservationId()).toList());
  }

  // 将预约信息加入数据库中
  private IndividualResponseDTO saveReservation(ReservationType type, Integer availabilityId, List<Integer> users, ReservationUserState state)
  {
    Reservation reservation = new Reservation(type, availabilityId);
    reservationRepository.save(reservation);
    List<ReservationUserDTO> result = addReservationUsers(reservation.getReservationId(), users, state, ReservationOperation.reserve);
    return new IndividualResponseDTO(reservation, result, "");
  }

  // 获取预约用户的详细信息
  private void getReservationUserInfo(List<ReservationUserDTO> users) throws Exception
  {
    for (int i = 0; i < users.size(); i++) {
      UserInfoDetailDTO userInfo = userService.getUserInfo(users.get(i).getUserId());
      users.set(i, new ReservationUserDTO(null, userInfo.getUserId(), userInfo.getUserName(), userInfo.getPhoto(), null,
        userInfo.getRealName(), userInfo.getPhone()));
    }
  }

  // 获取预约团体的详细信息并检查用户权限
  private ReservationGroupDTO getReservationGroup(Integer groupId, Integer userId) throws Exception
  {
    GroupDetailDTO groupDetail = groupService.getGroupDetail(groupId);
    for (GroupMemberDetailDTO member : groupDetail.getMembers()) {
      if(userId != null && member.getUserId() == userId){
        if(member.getRole().equals("leader")){
          break;
        }
        else{
          throw new ServiceException(401, "用户不是团体管理员");
        }
      }
    }
    return new ReservationGroupDTO(null, groupId, groupDetail.getGroupName());
  }

  private List<CourtAvailability> getAvailabilityByState(Integer timeslotId, List<Integer> courts, String state) throws Exception
  {
    List<CourtAvailability> availabilities = timeslotService.getAvailabilityByState(timeslotId, state);
    List<CourtAvailability> eligibleAvailabilities = new ArrayList<CourtAvailability>();
    for (CourtAvailability availability : availabilities) {
      if(courts.contains(availability.getCourtId())){
        eligibleAvailabilities.add(availability);
      }
    }
    return eligibleAvailabilities;
  }

  // 寻找拼场场地的函数
  private Integer findMatch(Integer reserveCount, Integer venueId, Integer timeslotId, String courtType) throws Exception
  {
    // 1. 找到符合条件的目标场地
    List<Integer> courts = courtService.findCourtByType(venueId, courtType);
    // 2. 找到符合条件的可拼场项
    List<CourtAvailability> availabilities = getAvailabilityByState(timeslotId, courts, "matching");
    if(availabilities.size() == 0){
      return -1; // 没有符合时间的可预约项
    }
    // 3. 检查容量限制
    for(CourtAvailability availability: availabilities){
      Optional<Reservation> reservation = reservationRepository.getMatchingReservation(availabilities.get(0).getAvailabilityId());
      if(reservation.isEmpty()){
        throw new ServiceException(500, "开放时间段与预约对应不一致");
      }
      Integer resultReservation = reservation.get().getReservationId();
      Optional<MatchReservation> matchReservation = matchReservationRepository.findByReservationId(resultReservation);
      if(matchReservation.isEmpty()){
        throw new ServiceException(500, "未找到对应的拼场预约");
      }
      Court court = courtService.getCourtById(availability.getCourtId());
      if(matchReservation.get().getReservedCount() + reserveCount <= court.getCapacity()){
        return resultReservation;
      }
    }
    return -1;
  }

  // 分配拼场场地的函数
  private MatchResponseDTO allocateMatch(MatchRequestDTO reservationInfo) throws Exception
  {
    // 1. 找到符合条件的目标场地
    List<Integer> courts = courtService.findCourtByType(reservationInfo.getVenueId(), reservationInfo.getCourtType());
    // 2. 找到符合条件的可预约项
    List<CourtAvailability> availabilities = getAvailabilityByState(reservationInfo.getTimeslotId(), courts, "reserveable");
    if(availabilities.size() == 0){
      throw new ServiceException(404, "该时间段没有可以拼场的场地");
    }
    CourtAvailability courtAvailability = availabilities.get(0);
    // 3. 向数据库中更新预约信息
    IndividualResponseDTO saveResult = saveReservation(ReservationType.match, courtAvailability.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationUserState.matching);
    // 4. 向数据库中更新拼场预约信息
    // Instant expirationTime = Instant.now().plus(Duration.ofSeconds(30));
    Instant expirationTime = Instant.now().plus(Duration.ofDays(2));
    MatchReservation matchResult = new MatchReservation(null, saveResult.getReservationInfo().getReservationId(),
    expirationTime, reservationInfo.getReservationCount());
    matchReservationRepository.save(matchResult);
    // 5. 更新开放时间段为拼场状态
    courtAvailability.setState(CourtAvailabilityState.matching);
    // venueController.patchAvailability(courtAvailability);
    timeslotService.patchAvailability(courtAvailability);
    // 6. 获取预约用户信息
    getReservationUserInfo(saveResult.getUsers());
    // 7. 向场地管理方申请拼场占用
    // occupyManagerConfirm(new ReservationRequestDTO(saveResult.getReservationInfo().getReservationId(), courtAvailability, saveResult.getUsers()));
    occupyManagerConfirm(courtAvailability.getAvailabilityId());
    // 8. 设置拼场到期逻辑
    scheduleMatchDue(matchResult);
    return new MatchResponseDTO(saveResult.getReservationInfo(), saveResult.getUsers(), matchResult);
  }

  // 加入拼场的函数
  private MatchResponseDTO joinMatch(MatchRequestDTO reservationInfo, Integer reservationId) throws Exception
  {
    // 1. 更新用户预约表
    Optional<Reservation> reservation = reservationRepository.findById(reservationId);
    if(reservation.isEmpty()){
      throw new ServiceException(404, "未找到预约信息");
    }
    Reservation targetReservation = reservation.get();
    List<ReservationUserDTO> userResult = addReservationUsers(targetReservation.getReservationId(), reservationInfo.getUsers(), ReservationUserState.matching, ReservationOperation.join);
    // 2. 查找用户预约信息
    // getReservationUserInfo(userResult);
    // 3. 更新拼场预约信息
    Optional<MatchReservation> matchReservation = matchReservationRepository.findByReservationId(reservationId);
    if(matchReservation.isEmpty()){
      throw new ServiceException(404, "未找到拼场预约信息");
    }
    MatchReservation matchResult = matchReservation.get();
    matchResult.setReservedCount(matchResult.getReservedCount() + reservationInfo.getReservationCount());
    matchReservationRepository.save(matchResult);
    targetReservation.setCourtAvailability(null);
    return new MatchResponseDTO(targetReservation, userResult, matchResult);
  }

  private void sendReservationMessage(Integer reservationId, List<ReservationUserDTO> users)
  {
    String notificationContent = "您已成功预约场地，预约ID为：" + reservationId.toString() + "，请按时到场";
    for(ReservationUserDTO user: users){
      userService.sendUserNotification(new NotificationContentDTO(NotificationType.reservation, "预约成功通知", notificationContent, user.getUserId()));
    }
  }

  // 个人预约交易函数
  @Transactional(rollbackFor = Exception.class)
  public IndividualResponseDTO individualReservation(IndividualRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 该函数为transaction函数，抛出异常即表示预约失败，所有操作均会撤销
    if(!violationService.checkViolationState(userId)){
      throw new ServiceException(422, "用户权限封禁中，无法进行预约");
    }
    // 1. 找到可预约项
    CourtAvailability targetAvailability = timeslotService.getAvailability(reservationInfo.getAvailabilityId());
    // 2. 插入预约项目
    IndividualResponseDTO saveResult = saveReservation(ReservationType.individual, reservationInfo.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationUserState.reserved);

    // 3. 更新场地可用状态
    targetAvailability.setState(CourtAvailabilityState.full);
    timeslotService.patchAvailability(targetAvailability);

    // 4. 确认预约信息
    String reservationMsg = reservationConfirm(saveResult.getReservationInfo(), saveResult.getUsers());
    saveResult.setMsg(reservationMsg);
    // 5. 发送通知
    sendReservationMessage(saveResult.getReservationInfo().getReservationId(), saveResult.getUsers());
    // 6. 隐藏用户姓名后返回给前端
    hideUserInfo(saveResult.getUsers());
    return saveResult;
  }

  // 团体预约交易函数
  @Transactional(rollbackFor = Exception.class)
  public GroupResponseDTO groupReservation(GroupRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 该函数为transaction函数，抛出异常即表示预约失败，所有操作均会撤销
    if(!violationService.checkViolationState(userId)){
      throw new ServiceException(422, "用户权限封禁中，无法进行预约");
    }

    // 1. 找到可预约项
    CourtAvailability targetAvailability = timeslotService.getAvailability(reservationInfo.getAvailabilityId());
    // 2. 插入预约项目
    IndividualResponseDTO saveResult = saveReservation(ReservationType.group, reservationInfo.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationUserState.reserved);
    // 3. 插入团体预约
    GroupReservation groupReservation = new GroupReservation(reservationInfo.getGroupId(), saveResult.getReservationInfo().getReservationId());
    groupReservationRepository.save(groupReservation);
    // 4. 更新场地可用状态
    targetAvailability.setState(CourtAvailabilityState.full);
    timeslotService.patchAvailability(targetAvailability);
    // 5. 获取预约的用户信息
    getReservationUserInfo(saveResult.getUsers());
    // 6. 获取预约的团体信息
    ReservationGroupDTO groupInfo = getReservationGroup(reservationInfo.getGroupId(), userId);
    groupInfo.setGroupReservationId(groupReservation.getGroupReservationId());
    // 7. 向场地管理方发送预约请求
    reservationConfirm(saveResult.getReservationInfo(), saveResult.getUsers());
    hideUserInfo(saveResult.getUsers());
    return new GroupResponseDTO(saveResult.getReservationInfo(), saveResult.getUsers(), groupInfo);
  }

  // 拼场预约交易函数
  @Transactional(rollbackFor = Exception.class)
  public MatchResponseDTO matchReservation(MatchRequestDTO reservationInfo, Integer userId) throws Exception
  {
    if(!violationService.checkViolationState(userId)){
      throw new ServiceException(422, "用户权限封禁中，无法进行预约");
    }
    // 1. 尝试查找可加入的拼场场地
    Timeslot timeslot = timeslotService.getTimeslotById(reservationInfo.getTimeslotId());
    reservationInfo.setVenueId(timeslot.getVenueId());
    Integer availableReservation = findMatch(reservationInfo.getReservationCount(), 
    reservationInfo.getVenueId(), reservationInfo.getTimeslotId(), reservationInfo.getCourtType());
    if(availableReservation == -1){
      // 2. 未找到拼场场地：申请分配一个拼场场地
      return allocateMatch(reservationInfo);
    }
    else{
      // 3. 找到拼场场地：尝试加入拼场场地
      return joinMatch(reservationInfo, availableReservation);
    }
  }

  // public MatchResponseDTO matchReservation(MatchRequestDTO reservationInfo) throws Exception
  // {
  //   MatchResponseDTO matchResult = matchReservationTransaction(reservationInfo);
  //   return matchResult;
  // }

  // 获取用户预约信息
  public Page<ReservationMetaDTO> getUserReservations(Integer userId, Integer page) throws Exception
  {
    Pageable pageable = PageRequest.of(page, ReservationPageCount);
    return userReservationRepository.getUserReservationsMeta(userId, pageable);
  }

  public ReservationDetailDTO getReservationDetail(Integer reservationId, Integer userId) throws Exception
  {
    // 1. 获取基本信息
    ReservationBasicDTO basicInfo = reservationRepository.getReservationDetail(reservationId);
    if(basicInfo.getType() == ReservationType.group){
      Group reservationGroup = groupReservationRepository.getReservationGroup(reservationId);
      basicInfo.setGroupId(reservationGroup.getGroupId());
      basicInfo.setGroupName(reservationGroup.getGroupName());
    }
    else if(basicInfo.getType() == ReservationType.match){
      Optional<MatchReservation> matchReservationOptional = matchReservationRepository.findByReservationId(reservationId);
      if(matchReservationOptional.isEmpty()){
        throw new ServiceException(404, "未找到拼场预约信息");
      }
      basicInfo.setExpirationTime(matchReservationOptional.get().getExpirationTime());
    }
    // 2. 获取预约用户信息
    List<ReservationUserDTO> userInfo = userReservationRepository.findAllByReservationId(reservationId).stream().map(userReservation -> {
      Boolean isAdmin = userService.isUserAdmin(userId);
      if(!isAdmin && userReservation.getUserId() == userId){
        basicInfo.setUserState(userReservation.getUserState());
      }
      return new ReservationUserDTO(
        userReservation.getUserReservationId(),
        userReservation.getUserId(),
        userReservation.getUser().getUserName(),
        userService.getUserPhoto(userReservation.getUserId()),
        userReservation.getUserState(),
        isAdmin ? userReservation.getUser().getRealName() : null,
        null
      );
    }).toList();
    // 3. 获取预约记录信息
    List<ReservationRecord> records = reservationRecordRepository.findAllByReservationId(reservationId);
    return new ReservationDetailDTO(basicInfo, userInfo, records);
  }

  public ResultMsg userCancelReservation(CancelReservationDTO cancelDto, Integer userId) throws Exception
  {
    cancelDto.setUserId(userId);
    ReservationOperation operation = ReservationOperation.usercancel;
    // 1. 更新预约状态
    if(cancelDto.getType() == CancelReservationType.all){
      Optional<Reservation> reservationOptional = reservationRepository.findById(cancelDto.getReservationId());
      if(reservationOptional.isEmpty()){
        throw new ServiceException(404, "未找到预约信息");
      }
      Reservation reservation = reservationOptional.get();
      reservation.setState(ReservationState.cancelled);
      reservationRepository.save(reservation);
      // 更新场地状态信息
      CourtAvailability courtAvailability = reservationRepository.getReservationCourtAvailability(cancelDto.getReservationId());
      timeslotService.changeAvailabilityState(courtAvailability, CourtAvailabilityState.reserveable);
      operation = ReservationOperation.cancelall;
    }
    else if(cancelDto.getType() == CancelReservationType.individual){
      if(userReservationRepository.countReservationUsers(cancelDto.getReservationId()) <= 1L){
        throw new ServiceException(422, "您是唯一一位预约的用户，请取消整个预约项目");
      }
      Optional<UserReservation> userReservationOptional = userReservationRepository.findByUserIdAndReservationId(cancelDto.getReservationId(), cancelDto.getUserId());
      if(userReservationOptional.isEmpty()){
        throw new ServiceException(404, "未找到要更改的用户信息");
      }
      UserReservation userReservation = userReservationOptional.get();
      userReservation.setUserState(ReservationUserState.cancelled);
      userReservationRepository.save(userReservation);
    }
    
    // 3. 写入预约记录
    ReservationRecord record = new ReservationRecord(null, operation, Instant.now(), cancelDto.getUserId(), cancelDto.getReservationId());
    reservationRecordRepository.save(record);
    return ResultMsg.success("取消预约成功");
  }
}
