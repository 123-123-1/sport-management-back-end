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

import com.tongji.sportmanagement.AccountSubsystem.DTO.NotificationContentDTO;
import com.tongji.sportmanagement.AccountSubsystem.DTO.UserInfoDetailDTO;
import com.tongji.sportmanagement.AccountSubsystem.Entity.NotificationType;
import com.tongji.sportmanagement.AccountSubsystem.Service.UserService;
import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Controller.ManagementController;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationRequestDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ReservationResponseDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupMemberDetailDTO;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupService;
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
import com.tongji.sportmanagement.VenueSubsystem.Service.CourtService;
import com.tongji.sportmanagement.VenueSubsystem.Service.TimeslotService;

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
  private TimeslotService timeslotService; // 用于获取开放时间信息
  @Autowired
  private CourtService courtService; // 用于获取场地信息
  @Autowired
  private UserService userService; // 用于获取用户信息
  @Autowired
  private GroupService groupService; // 用于获取团体信息

  final static int ReservationPageCount = 10;

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
    // ResponseEntity<ReservationResponseDTO> managerResponse = managementController.sendOccupyRequest(requestDTO);
    // if(!isValidRequest(managerResponse, ReservationResponseDTO.class)){
    //   throw new ServiceException(500, "场地管理方未给出有效的预约结果");
    // }
    // ReservationResponseDTO managerInfo = (ReservationResponseDTO)managerResponse.getBody();
    // if(managerInfo.getStatus() == 0){
    //   throw new ServiceException(409, managerInfo.getMsg());
    // }
    // // 未抛出异常，预约成功
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
  private List<ReservationUserDTO> addReservationUsers(Integer reservationId, List<Integer> users, ReservationUserState state)
  {
    ArrayList<ReservationUserDTO> result = new ArrayList<ReservationUserDTO>();
    Instant reservationTime = Instant.now();
    for (Integer user : users) {
      UserReservation userReservation = new UserReservation(null, user, state, reservationId);
      userReservationRepository.save(userReservation);
      reservationRecordRepository.save(new ReservationRecord(null, ReservationUserState.reserved, reservationTime, user, reservationId));
      ReservationUserDTO userResult = new ReservationUserDTO();
      userResult.setUserId(user);
      userResult.setUserReservationId(userReservation.getUserReservationId());
      result.add(userResult);
    }
    return result;
  }

  // 将预约信息加入数据库中
  private IndividualResponseDTO saveReservation(ReservationType type, Integer availabilityId, List<Integer> users, ReservationUserState state)
  {
    Reservation reservation = new Reservation(type, availabilityId);
    reservationRepository.save(reservation);
    List<ReservationUserDTO> result = addReservationUsers(reservation.getReservationId(), users, state);
    return new IndividualResponseDTO(reservation, result);
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

  private List<Integer> findCourtByType(Integer venueId, String courtType) throws Exception
  {
    // List<Court> venueCourts = courtService.getVenueCourts(venueId);
    // List<Integer> eligibleCourts = new ArrayList<Integer>();
    // for (Court court : venueCourts) {
    //   if(court.getType().equals(courtType)){
    //     eligibleCourts.add(court.getCourtId());
    //   }
    // }
    // return eligibleCourts;
    return new ArrayList<>();
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
    List<Integer> courts = findCourtByType(reservationInfo.getVenueId(), reservationInfo.getCourtType());
    // 2. 找到符合条件的可预约项
    List<CourtAvailability> availabilities = getAvailabilityByState(reservationInfo.getTimeslotId(), courts, "reserveable");
    if(availabilities.size() == 0){
      throw new ServiceException(404, "未找到可用的拼场场地");
    }
    CourtAvailability courtAvailability = availabilities.get(0);
    // 3. 向数据库中更新预约信息
    IndividualResponseDTO saveResult = saveReservation(ReservationType.match, courtAvailability.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationUserState.matching);
    // 4. 向数据库中更新拼场预约信息
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
    List<ReservationUserDTO> userResult = addReservationUsers(targetReservation.getReservationId(), reservationInfo.getUsers(), ReservationUserState.matching);
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
      userService.sendUserNotification(new NotificationContentDTO(NotificationType.reservation, "预约成功通知", notificationContent, user.getUserId()));
    }
  }

  // 个人预约交易函数
  @Transactional
  private IndividualResponseDTO individualReservationTransaction(IndividualRequestDTO reservationInfo, Integer userId) throws Exception
  {
    // 该函数为transaction函数，抛出异常即表示预约失败，所有操作均会撤销
    // 1. 找到可预约项
    CourtAvailability targetAvailability = timeslotService.getAvailability(reservationInfo.getAvailabilityId());
    // 2. 插入预约项目
    IndividualResponseDTO saveResult = saveReservation(ReservationType.individual, reservationInfo.getAvailabilityId(),
    reservationInfo.getUsers(), ReservationUserState.reserved);
    // 3. 获取预约的用户信息
    getReservationUserInfo(saveResult.getUsers());
    // 4. 更新场地可用状态
    targetAvailability.setState(CourtAvailabilityState.full);
    timeslotService.patchAvailability(targetAvailability);
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

  // 获取用户预约信息
  public List<ReservationMetaDTO> getUserReservations(Integer userId) throws Exception
  {
    return userReservationRepository.getUserReservationsMeta(userId);
  }

  public ReservationDetailDTO getReservationDetail(Integer reservationId, Integer userId) throws Exception
  {
    // 1. 获取基本信息
    ReservationBasicDTO basicInfo = reservationRepository.getReservationDetail(reservationId);
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
}
