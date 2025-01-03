package com.tongji.sportmanagement.ReservationSubsystem.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.AccountSubsystem.Controller.UserController;
import com.tongji.sportmanagement.AccountSubsystem.DTO.NotificationContentDTO;
import com.tongji.sportmanagement.AccountSubsystem.DTO.UserInfoDetailDTO;
import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationType;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.DTO.UserProfileDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Controller.ManagementController;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationRequestDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationResponseDTO;
import com.tongji.sportmanagement.GroupSubsystem.Controller.GroupController;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
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
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationRecord;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationState;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.ReservationType;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.GroupReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.MatchReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRecordRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.ReservationRepository;
import com.tongji.sportmanagement.ReservationSubsystem.Repository.UserReservationRepository;
import com.tongji.sportmanagement.VenueSubsystem.Controller.VenueController;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailabilityState;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;

import jakarta.transaction.Transactional;

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
  private VenueController venueController; // 用于获取预约时间段信息
  @Autowired
  private UserController userController; // 用于获取用户信息
  @Autowired
  private GroupController groupController;

  // ---------- 工具函数：用于封装预约流程 ----------
  private <T> boolean isValidRequest(ResponseEntity<T> response, Class<?> expectClass)
  {
    Object responseBody = response.getBody();
    return response.getStatusCode() == HttpStatusCode.valueOf(200) && responseBody != null 
      && expectClass.isInstance(responseBody);
  }


  // 发送请求向场地管理方确认预约
  private void reservationManagerConfirm(ReservationRequestDTO requestDTO) throws Exception
  {
    ResponseEntity<ReservationResponseDTO> managerResponse = managementController.sendReservationRequest(requestDTO);
    if(!isValidRequest(managerResponse, ReservationResponseDTO.class)){
      throw new ServiceException(500, "场地管理方未给出有效的预约结果");
    }
    ReservationResponseDTO managerInfo = (ReservationResponseDTO)managerResponse.getBody();
    if(managerInfo.getStatus() == 0){
      throw new ServiceException(409, managerInfo.getMsg());
    }
    // 未抛出异常，预约成功
  }

  // 发送请求向场地管理方申请占用
  private void occupyManagerConfirm(ReservationRequestDTO requestDTO) throws Exception
  {
    ResponseEntity<ReservationResponseDTO> managerResponse = managementController.sendOccupyRequest(requestDTO);
    if(!isValidRequest(managerResponse, ReservationResponseDTO.class)){
      throw new ServiceException(500, "场地管理方未给出有效的预约结果");
    }
    ReservationResponseDTO managerInfo = (ReservationResponseDTO)managerResponse.getBody();
    if(managerInfo.getStatus() == 0){
      throw new ServiceException(409, managerInfo.getMsg());
    }
    // 未抛出异常，预约成功
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
  private List<ReservationUserDTO> addReservationUsers(Reservation reservation, List<Integer> users, ReservationState state)
  {
    ArrayList<ReservationUserDTO> result = new ArrayList<ReservationUserDTO>();
    Instant reservationTime = Instant.now();
    for (Integer user : users) {
      UserReservation userReservation = new UserReservation(null, user, state, reservation);
      userReservationRepository.save(userReservation);
      reservationRecordRepository.save(new ReservationRecord(null, ReservationState.reserved, reservationTime, user, reservation.getReservationId()));
      ReservationUserDTO userResult = new ReservationUserDTO();
      userResult.setUserId(user);
      userResult.setUserReservationId(userReservation.getReservation().getReservationId());
      result.add(userResult);
    }
    return result;
  }

  // 将预约信息加入数据库中
  private IndividualResponseDTO saveReservation(ReservationType type, Integer availabilityId, List<Integer> users, ReservationState state)
  {
    Reservation reservation = new Reservation(null, type, availabilityId);
    reservationRepository.save(reservation);
    List<ReservationUserDTO> result = addReservationUsers(reservation, users, state);
    return new IndividualResponseDTO(reservation, result);
  }

  // 查找可预约项信息
  private CourtAvailability findAvailability(Integer availabilityId) throws Exception
  {
    ResponseEntity<Object> availabilityResponse = venueController.getVenueAvailability(availabilityId);
    if(!isValidRequest(availabilityResponse, CourtAvailability.class)){
      throw new ServiceException(availabilityResponse.getStatusCode().value(), "获取可预约项失败");
    }
    return (CourtAvailability)availabilityResponse.getBody();
  }

  // 获取预约用户的详细信息
  private void getReservationUserInfo(List<ReservationUserDTO> users) throws Exception
  {
    for (int i = 0; i < users.size(); i++) {
      ResponseEntity<Object> userInfoResponse = userController.getUserInfo(users.get(i).getUserId());
      if(!isValidRequest(userInfoResponse, UserInfoDetailDTO.class)){
        throw new ServiceException(404, "未找到用户信息");
      }
      UserInfoDetailDTO userInfo = (UserInfoDetailDTO) userInfoResponse.getBody();
      users.set(i, new ReservationUserDTO(null, userInfo.getUserId(), userInfo.getUserName(), userInfo.getPhoto(),
        userInfo.getRealName(), userInfo.getPhone()));
    }
  }

  // 获取预约团体的详细信息并检查用户权限
  private ReservationGroupDTO getReservationGroup(Integer groupId, Integer userId) throws Exception
  {
    ResponseEntity<Object> groupResponse = groupController.getGroupByID(groupId);
    if(!isValidRequest(groupResponse, GroupDetailDTO.class)){
      throw new ServiceException(404, "未找到团体信息");
    }
    GroupDetailDTO groupDetail = (GroupDetailDTO)groupResponse.getBody();
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

  private List<Integer> findCourtByType(Integer venueId, String courtType) throws Exception
  {
    ResponseEntity<Object> courtResponse = venueController.getVenueCourts(venueId);
    if(!isValidRequest(courtResponse, List.class)){
      throw new ServiceException(404, "未找到场地信息");
    }
    List<?> venueCourts = (List<?>)courtResponse.getBody();
    List<Integer> eligibleCourts = new ArrayList<Integer>();
    for (Object object : venueCourts) {
      if(!(object instanceof Court)){
        throw new ServiceException(500, "获取场地信息错误");
      }
      Court curCourt = (Court)object;
      if(curCourt.getType().equals(courtType)){
        eligibleCourts.add(curCourt.getCourtId());
      }
    }
    return eligibleCourts;
  }

  private List<CourtAvailability> getAvailabilityByState(Integer timeslotId, List<Integer> courts, String state) throws Exception
  {
    ResponseEntity<Object> availabilityResponse = venueController.getAvalibilityByState(timeslotId, state);
    if(!isValidRequest(availabilityResponse, List.class)){
      throw new ServiceException(404, "未找到场地信息");
    }
    List<?> availabilities = (List<?>)availabilityResponse.getBody();
    List<CourtAvailability> eligibleAvailabilities = new ArrayList<CourtAvailability>();
    for (Object object : availabilities) {
      if(!(object instanceof CourtAvailability)){
        throw new ServiceException(500, "获取预约项错误");
      }
      CourtAvailability curAvailability = (CourtAvailability)object;
      if(courts.contains(curAvailability.getCourtId())){
        eligibleAvailabilities.add(curAvailability);
      }
    }
    return eligibleAvailabilities;
  }

  // 根据场地ID查找场地
  private Court getCourtById(Integer courtId) throws Exception
  {
    ResponseEntity<Object> courtResponse = venueController.getCourtById(courtId);
    if(!isValidRequest(courtResponse, Court.class)){
      throw new ServiceException(404, "未找到场地信息");
    }
    return (Court)courtResponse.getBody();
  }

  // 根据时间段ID查找时间段
  private Timeslot getTimeslotById(Integer timeslotId) throws Exception
  {
    ResponseEntity<Object> timeslotResponse = venueController.getTimeslotById(timeslotId);
    if(!isValidRequest(timeslotResponse, Timeslot.class)){
      throw new ServiceException(404, "未找到场地信息");
    }
    return (Timeslot)timeslotResponse.getBody();
  }

  // 根据场地ID查找场地
  private Venue getVenueById(Integer venueId) throws Exception
  {
    ResponseEntity<Object> venueResponse = venueController.getVenueDetail(venueId);
    if(!isValidRequest(venueResponse, Venue.class)){
      throw new ServiceException(404, "未找到场地信息");
    }
    return (Venue) venueResponse.getBody();
  }

  // 寻找拼场场地的函数
  private Integer findMatch(Integer reserveCount, Integer venueId, Integer timeslotId, String courtType) throws Exception
  {
    // 1. 找到符合条件的目标场地
    List<Integer> courts = findCourtByType(venueId, courtType);
    // 2. 找到符合条件的可拼场项
    List<CourtAvailability> availabilities = getAvailabilityByState(timeslotId, courts, "matching");
    if(availabilities.size() == 0){
      return -1; // 没有符合时间的可预约项
    }
    // 3. 检查容量限制
    for(CourtAvailability availability: availabilities){
      Optional<Reservation> reservation = reservationRepository.findByAvailabilityId(availabilities.get(0).getAvailabilityId());
      if(reservation.isEmpty()){
        throw new ServiceException(500, "开放时间段与预约对应不一致");
      }
      Integer resultReservation = reservation.get().getReservationId();
      Optional<MatchReservation> matchReservation = matchReservationRepository.findByReservationId(resultReservation);
      if(matchReservation.isEmpty()){
        throw new ServiceException(500, "未找到对应的拼场预约");
      }
      Court court = getCourtById(availability.getCourtId());
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
    List<Integer> courts = findCourtByType(reservationInfo.getVenueId(), reservationInfo.getCourtType());
    // 2. 找到符合条件的可预约项
    List<CourtAvailability> availabilities = getAvailabilityByState(reservationInfo.getTimeslotId(), courts, "reserveable");
    if(availabilities.size() == 0){
      throw new ServiceException(404, "未找到可用的拼场场地");
    }
    CourtAvailability courtAvailability = availabilities.get(0);
    // 3. 向数据库中更新预约信息
    IndividualResponseDTO saveResult = saveReservation(ReservationType.match, courtAvailability.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationState.matching);
    // 4. 向数据库中更新拼场预约信息
    Instant expirationTime = Instant.now().plus(Duration.ofDays(2));
    MatchReservation matchResult = new MatchReservation(null, saveResult.getReservationInfo().getReservationId(),
    expirationTime, reservationInfo.getReservationCount());
    matchReservationRepository.save(matchResult);
    // 5. 更新开放时间段为拼场状态
    courtAvailability.setState(CourtAvailabilityState.matching);
    venueController.patchAvailability(courtAvailability);
    // 6. 获取预约用户信息
    getReservationUserInfo(saveResult.getUsers());
    // 7. 向场地管理方申请拼场占用
    occupyManagerConfirm(new ReservationRequestDTO(saveResult.getReservationInfo().getReservationId(), courtAvailability, saveResult.getUsers()));
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
    List<ReservationUserDTO> userResult = addReservationUsers(targetReservation, reservationInfo.getUsers(), ReservationState.matching);
    // 2. 查找用户预约信息
    getReservationUserInfo(userResult);
    // 3. 更新拼场预约信息
    Optional<MatchReservation> matchReservation = matchReservationRepository.findByReservationId(reservationId);
    if(matchReservation.isEmpty()){
      throw new ServiceException(404, "未找到拼场预约信息");
    }
    MatchReservation matchResult = matchReservation.get();
    matchResult.setReservedCount(matchResult.getReservedCount() + reservationInfo.getReservationCount());
    matchReservationRepository.save(matchResult);
    return new MatchResponseDTO(targetReservation, userResult, matchResult);
  }

  private void sendReservationMessage(Integer reservationId, List<ReservationUserDTO> users)
  {
    String notificationContent = "您已成功预约场地，预约ID为：" + reservationId.toString() + "，请按时到场";
    for(ReservationUserDTO user: users){
      userController.sendUserNotifications(new NotificationContentDTO(NotificationType.reservation, "预约成功通知", notificationContent, user.getUserId()));
    }
  }

  // 个人预约交易函数
  @Transactional
  private IndividualResponseDTO individualReservationTransaction(IndividualRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 该函数为transaction函数，抛出异常即表示预约失败，所有操作均会撤销
    // 1. 找到可预约项
    CourtAvailability targetAvailability = findAvailability(reservationInfo.getAvailabilityId());
    // 2. 插入预约项目
    IndividualResponseDTO saveResult = saveReservation(ReservationType.individual, reservationInfo.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationState.reserved);
    // 3. 获取预约的用户信息
    getReservationUserInfo(saveResult.getUsers());
    // 4. 更新场地可用状态
    targetAvailability.setState(CourtAvailabilityState.full);
    venueController.patchAvailability(targetAvailability);
    // 5. 向场地管理方发送预约请求
    reservationManagerConfirm(new ReservationRequestDTO(saveResult.getReservationInfo().getReservationId(), targetAvailability, saveResult.getUsers()));
    return saveResult;
  }

  public IndividualResponseDTO individualReservation(IndividualRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 1-4. 执行预约交易操作
    IndividualResponseDTO saveResult = individualReservationTransaction(reservationInfo, userId);
    // 5. 发送通知
    sendReservationMessage(saveResult.getReservationInfo().getReservationId(), saveResult.getUsers());
    // 6. 隐藏用户姓名后返回给前端
    hideUserInfo(saveResult.getUsers());
    return saveResult;
  }

  // 团体预约交易函数
  @Transactional
  private GroupResponseDTO groupReservationTransaction(GroupRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 该函数为transaction函数，抛出异常即表示预约失败，所有操作均会撤销
    // 1. 找到可预约项
    CourtAvailability targetAvailability = findAvailability(reservationInfo.getAvailabilityId());
    // 2. 插入预约项目
    IndividualResponseDTO saveResult = saveReservation(ReservationType.group, reservationInfo.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationState.reserved);
    // 3. 插入团体预约
    GroupReservation groupReservation = new GroupReservation(null, reservationInfo.getGroupId(), saveResult.getReservationInfo());
    groupReservationRepository.save(groupReservation);
    // 4. 更新场地可用状态
    targetAvailability.setState(CourtAvailabilityState.full);
    venueController.patchAvailability(targetAvailability);
    // 5. 获取预约的用户信息
    getReservationUserInfo(saveResult.getUsers());
    // 6. 获取预约的团体信息
    ReservationGroupDTO groupInfo = getReservationGroup(reservationInfo.getGroupId(), userId);
    groupInfo.setGroupReservationId(groupReservation.getGroupReservationId());
    // 7. 向场地管理方发送预约请求
    reservationManagerConfirm(new ReservationRequestDTO(saveResult.getReservationInfo().getReservationId(), targetAvailability, saveResult.getUsers()));
    return new GroupResponseDTO(saveResult.getReservationInfo(), saveResult.getUsers(), groupInfo);
  }

  public GroupResponseDTO groupReservation(GroupRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 1-7. 执行交易操作
    GroupResponseDTO reservationResult = groupReservationTransaction(reservationInfo, userId);
    // 8. 隐藏用户姓名后返回给前端
    hideUserInfo(reservationResult.getUsers());
    return reservationResult;
  }

  // 拼场预约交易函数
  @Transactional
  private MatchResponseDTO matchReservationTransaction(MatchRequestDTO reservationInfo) throws Exception
  {
    // 1. 尝试查找可加入的拼场场地
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

  public MatchResponseDTO matchReservation(MatchRequestDTO reservationInfo) throws Exception
  {
    MatchResponseDTO matchResult = matchReservationTransaction(reservationInfo);
    return matchResult;
  }

  public List<ReservationMetaDTO> getUserReservations(Integer userId) throws Exception
  {
    List<ReservationMetaDTO> result = new ArrayList<ReservationMetaDTO>();
    // 1. 找到用户的所有预约
    List<UserReservation> userReservations = (List<UserReservation>)userReservationRepository.findAllByUserId(userId);
    // 2. 遍历每一个用户预约查找信息
    for(UserReservation userReservation: userReservations){
      Reservation reservation = userReservation.getReservation();
      // 3. 找到可预约项
      CourtAvailability availability = findAvailability(reservation.getAvailabilityId());
      System.out.println(availability.getCourtId());
      // 4. 找到场地
      Court court = getCourtById(availability.getCourtId());
      // 5. 找到场馆信息
      Venue venue = getVenueById(court.getVenueId());
      // 6. 找到可用时间段信息
      Timeslot timeslot = getTimeslotById(availability.getTimeslotId());
      // 7. 拼合预约信息
      result.add(new ReservationMetaDTO(reservation.getReservationId(), venue.getVenueName(), court.getCourtName(),
      timeslot.getStartTime(), timeslot.getEndTime(), reservation.getType(), userReservation.getState()));
    }
    return result;
  }

  public ReservationDetailDTO getReservationDetail(Integer reservationId, Integer userId) throws Exception
  {
    // 1. 获取基本信息
    ReservationBasicDTO basicInfo = new ReservationBasicDTO();
    basicInfo.setReservationId(reservationId);
    // (1) 获取预约
    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
    if(optionalReservation.isEmpty()){
      throw new ServiceException(404, "未找到预约信息");
    }
    Reservation reservation = optionalReservation.get();
    basicInfo.setReservationId(reservationId);
    basicInfo.setType(reservation.getType());
    // (2) 获取预约项
    CourtAvailability availability = findAvailability(reservation.getAvailabilityId());
    // (3) 获取场地信息
    Court court = getCourtById(availability.getCourtId());
    basicInfo.setCourtId(court.getCourtId());
    basicInfo.setCourtName(court.getCourtName());
    // (4) 获取开放时间段
    Timeslot timeslot = getTimeslotById(availability.getTimeslotId());
    basicInfo.setStartTime(timeslot.getStartTime());
    basicInfo.setEndTime(timeslot.getEndTime());
    // (5) 获取场馆信息
    Venue venue = getVenueById(court.getVenueId());
    basicInfo.setVenueId(venue.getVenueId());
    basicInfo.setVenueName(venue.getVenueName());
    // (6) 获取团体信息
    if(reservation.getType().equals(ReservationType.group)){
      Optional<GroupReservation> groupReservation = groupReservationRepository.findByReservationId(reservationId);
      if(groupReservation.isEmpty()){
        throw new ServiceException(404, "未找到团体预约信息");
      }
      ReservationGroupDTO group = getReservationGroup(groupReservation.get().getGroupId(), null);
      basicInfo.setGroupId(group.getGroupId());
      basicInfo.setGroupName(group.getGroupName());
    }
    // (7) 获取拼场信息
    else if(reservation.getType().equals(ReservationType.match)){
      Optional<MatchReservation> optionalMatch = matchReservationRepository.findByReservationId(reservationId);
      if(optionalMatch.isEmpty()){
        throw new ServiceException(404, "未找到拼场预约信息");
      }
      MatchReservation matchReservation = optionalMatch.get();
      basicInfo.setReservedCount(matchReservation.getReservedCount());
      basicInfo.setExpirationTime(matchReservation.getExpirationTime());
    }
    // 2. 获取预约用户信息
    List<UserReservation> userReservations = (List<UserReservation>)userReservationRepository.findAllByReservationId(reservationId);
    List<ReservationUserDTO> userInfo = new ArrayList<ReservationUserDTO>();
    for(UserReservation userReservation : userReservations){
      UserProfileDTO userProfile = userController.getUserProfile(userReservation.getUserId());
      userInfo.add(new ReservationUserDTO(userReservation.getUserReservationId(), userReservation.getUserId(),
      userProfile.getUserName(), userProfile.getPhoto(), null, null));
      if(userReservation.getUserId() == userId){
        basicInfo.setState(userReservation.getState());
      }
    }
    // 3. 获取预约记录信息
    List<ReservationRecord> records = (List<ReservationRecord>)reservationRecordRepository.findAllByReservationId(reservationId);
    return new ReservationDetailDTO(basicInfo, userInfo, records);
  }
}
