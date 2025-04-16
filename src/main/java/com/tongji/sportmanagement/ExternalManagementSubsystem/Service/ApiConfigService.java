package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigCreateDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigFieldsDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigResponseDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiRequestFieldDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiResponseFieldDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiOperationType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Repository.ApiConfigRepository;
import com.tongji.sportmanagement.ReservationSubsystem.DTO.ReservationUserDTO;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;
import com.tongji.sportmanagement.VenueSubsystem.Service.CourtService;
import com.tongji.sportmanagement.VenueSubsystem.Service.TimeslotService;

import jakarta.transaction.Transactional;

@Service
public class ApiConfigService
{

  @Autowired
  private ApiConfigRepository apiConfigRepository;

  @Autowired
  private ManagementUtilsService managementUtilsService;
  // 以下service用于填写API相关信息时获取相应数据
  @Autowired
  private TimeslotService timeslotService;
  @Autowired
  private CourtService courtService;


  // API配置参数常量
  private final ApiConfigFieldsDTO reservationConfigFields = new ApiConfigFieldsDTO(
    new ApiRequestFieldDTO[] {
      new ApiRequestFieldDTO("reservation_id_number", "怡运动系统为该预约分配的ID（数字类型）", "", "Number",  1),
      new ApiRequestFieldDTO("reservation_id_str", "怡运动系统为该预约分配的ID（字符串类型）", "", "String", "1"),
      new ApiRequestFieldDTO("user_info", "预约的用户信息", "用户信息数组的格式遵循用户信息配置的格式", "Array", List.of()),
      new ApiRequestFieldDTO("court_id_number", "用户预约的场地在怡运动系统的ID（数字类型）", "", "Number", 2),
      new ApiRequestFieldDTO("court_id_str", "用户预约的场地在怡运动系统的ID（字符串类型）", "", "String" , "2"),
      new ApiRequestFieldDTO("court_name", "用户预约的场地名称", "", "String", "场地1"),
      new ApiRequestFieldDTO("reservation_timeslot_id_number", "用户预约的时间段在怡运动系统中的ID（数字类型）", "", "Number", 3),
      new ApiRequestFieldDTO("reservation_timeslot_id_str", "用户预约的时间段在怡运动系统中的ID（字符串类型）", "", "String", "3"),
      new ApiRequestFieldDTO("reservation_start_time_str", "用户预约的时间段的开始时间", "字符串格式为YYYY-MM-DDThh:mm", "String", "2025-05-01T16:00"),
      new ApiRequestFieldDTO("reservation_end_time_str", "用户预约的时间段的结束时间", "字符串格式为YYYY-MM-DDThh:mm", "String", "2025-05-01T17:00"),
      new ApiRequestFieldDTO("reservation_start_time_date", "用户预约的时间段的开始时间", "表示从1970-01-01到预约时间的毫秒数", "Number", 1746086400000L),
      new ApiRequestFieldDTO("reservation_end_time_date", "用户预约的时间段的结束时间", "表示从1970-01-01到预约时间的毫秒数", "Number", 1746090000000L),
      new ApiRequestFieldDTO("reservation_price", "用户预约的场地价格", "", "Number", 12.11),
      new ApiRequestFieldDTO("reservation_type", "用户的预约类型", "individual表示个人预约，group表示团体预约，match表示拼场预约", "String", "individual")
    },
    new ApiResponseFieldDTO[] {
      new ApiResponseFieldDTO("status", true, "预约操作的结果，1表示预约成功，0表示预约失败", "Number | String", 1),
      new ApiResponseFieldDTO("fail_msg", false, "预约结果的失败原因（可选）","String", "预约发生错误"),
      new ApiResponseFieldDTO("success_msg", false, "预约结果的成功说明（可选）", "String", "您已成功预约场地")
    }
  );

  private final ApiConfigFieldsDTO userDataConfigFields = new ApiConfigFieldsDTO(
    new ApiRequestFieldDTO[]{
      new ApiRequestFieldDTO("user_id_number", "用户在怡运动中的ID（数字类型）", "", "Number", 1),
      new ApiRequestFieldDTO("user_id_str", "用户在怡运动中的ID（字符串类型）", "", "String", "1"),
      new ApiRequestFieldDTO("user_name", "用户在怡运动中的用户名", "", "String", "username"),
      new ApiRequestFieldDTO("real_name", "用户的真实姓名", "", "String", "姓名"),
      new ApiRequestFieldDTO("phone", "用户的联系方式", "", "String", "11111111111")
    },
    null
  );

  // private final ApiConfigFieldsDTO cancelConfigFields = new ApiConfigFieldsDTO(
  //   new ApiRequestFieldDTO[] {
  //     new ApiRequestFieldDTO("reservation_id_number", "怡运动系统为该预约分配的ID（数字类型）", "", "Number",  1),
  //     new ApiRequestFieldDTO("reservation_id_str", "怡运动系统为该预约分配的ID（字符串类型）", "", "String", "1"),
  //     new ApiRequestFieldDTO("user_id_number", "申请进行取消预约操作的用户ID（数字类型）", "", "Number", 2),
  //     new ApiRequestFieldDTO("user_id_str", "申请进行取消预约操作的用户ID（字符串类型）", "", "String", "2"),
  //     new ApiRequestFieldDTO("user_name", "申请取消预约操作的用户名", "", "String", "username"),
  //     new ApiRequestFieldDTO("user_real_name", "申请取消预约操作的用户真实姓名", "", "String", ""),
  //     new ApiRequestFieldDTO("type", "取消预约的类型", "all为取消整个预约项目，individual为取消个人预约", "String", "all")
  //   }, 
  //   new ApiResponseFieldDTO[]{
  //     new ApiResponseFieldDTO("status", true, "取消预约操作的结果，1表示预约成功，0表示预约失败", "Number | String", 1),
  //     new ApiResponseFieldDTO("fail_msg", false, "取消预约失败原因（可选）","String", "预约发生错误"),
  //     new ApiResponseFieldDTO("success_msg", false, "取消预约的成功说明（可选）", "String", "您已成功预约场地")
  //   });
  private final ApiConfigFieldsDTO occupyConfigFields = new ApiConfigFieldsDTO(
    new ApiRequestFieldDTO[]{
      new ApiRequestFieldDTO("availability_id_number", "要占用的预约项ID（数字形式）", "", "Number", 1),
      new ApiRequestFieldDTO("availability_id_str", "要占用的预约项ID（字符串形式）", "", "String", "1"),
      new ApiRequestFieldDTO("court_id_number", "用户预约的场地在怡运动系统的ID（数字类型）", "", "Number", 2),
      new ApiRequestFieldDTO("court_id_str", "用户预约的场地在怡运动系统的ID（字符串类型）", "", "String" , "2"),
      new ApiRequestFieldDTO("court_name", "用户预约的场地名称", "", "String", "场地1"),
      new ApiRequestFieldDTO("timeslot_id_number", "要占用的时间段ID（数字形式）", "", "Number", 2),
      new ApiRequestFieldDTO("timeslot_id_str", "要占用的时间段ID（字符串形式）", "", "String", "2"),
      new ApiRequestFieldDTO("start_time_str", "用户预约的时间段的开始时间", "字符串格式为YYYY-MM-DDThh:mm", "String", "2025-05-01T16:00"),
      new ApiRequestFieldDTO("end_time_str", "用户预约的时间段的结束时间", "字符串格式为YYYY-MM-DDThh:mm", "String", "2025-05-01T17:00"),
      new ApiRequestFieldDTO("start_time_date", "用户预约的时间段的开始时间", "表示从1970-01-01到预约时间的毫秒数", "Number", 1746086400000L),
      new ApiRequestFieldDTO("end_time_date", "用户预约的时间段的结束时间", "表示从1970-01-01到预约时间的毫秒数", "Number", 1746090000000L),
    },
    new ApiResponseFieldDTO[]{
      new ApiResponseFieldDTO("status", true, "场地占用操作的结果，1表示占用场地成功，0表示占用场地失败", "Number | String", 1)
    }
  );

  public ApiConfigFieldsDTO getConfigFields(ApiType type) throws Exception
  {
    switch (type) {
      case reservation:
        return reservationConfigFields;
      case userinfo:
        return userDataConfigFields;
      case occupy:
        return occupyConfigFields;
    }
    throw new ServiceException(422, "不支持的API配置类型");
  }

  public ApiConfigResponseDTO getConfigByManager(ApiType type, Integer managerId) throws Exception
  {
    if(type == null){
      throw new ServiceException(400, "API类型参数错误");
    }
    Integer venueId = managementUtilsService.getVenueIdByManager(managerId);
    Optional<ApiConfig> configOptional = apiConfigRepository.findByVenueIdAndType(venueId, type);
    return new ApiConfigResponseDTO(configOptional.isPresent() ? 1 : 0, configOptional);
  }

  public String getVenueUrl(Integer venueId, ApiType type) throws Exception
  {
    Optional<ApiConfig> configOptional = apiConfigRepository.findByVenueIdAndType(venueId, type);
    if(configOptional.isEmpty()){
      throw new ServiceException(404, "未找到配置项信息");
    }
    return configOptional.get().getApiUrl();
  }

  public ApiConfig createApiConfig(ApiConfigCreateDTO createInfo, Integer managerId) throws Exception
  {
    if(createInfo.getType() == null){
      throw new ServiceException(400, "API类型参数错误");
    }
    Integer venueId = managementUtilsService.getVenueIdByManager(managerId);
    ApiConfig config = new ApiConfig();
    config.setVenueId(venueId);
    config.setType(createInfo.getType());
    config.setApiUrl("");
    config.setOperationType(ApiOperationType.auto);
    config.setRequestContent("");
    config.setResponseContent("");
    apiConfigRepository.save(config);
    return config;
  }

  @Transactional
  public ResultMsg deleteApiConfig(Integer apiConfigId)
  {
    apiConfigRepository.deleteById(apiConfigId);
    return ResultMsg.success("已删除配置项");
  }

  @Transactional
  public ApiConfig editApiConfig(ApiConfig config) throws Exception
  {
    Optional<ApiConfig> configOptional = apiConfigRepository.findById(config.getApiconfigId());
    if(configOptional.isEmpty()){
      throw new ServiceException(404, "未找到对应的配置项");
    }
    ApiConfig editedConfig = configOptional.get();
    SportManagementUtils.copyNotNullProperties(config, editedConfig);
    apiConfigRepository.save(editedConfig);
    return editedConfig;
  }

  public ApiOperationType getReservationStrategy(Integer venueId, ApiType type)
  {
    Optional<ApiConfig> apiConfigOptional = apiConfigRepository.findByVenueIdAndType(venueId, type);
    if(apiConfigOptional.isEmpty()){
      return ApiOperationType.auto;
    }
    return apiConfigOptional.get().getOperationType();
  }

  String generateUserData(String tpl, ReservationUserDTO userData)
  {
    final String[] userDataFields = new String[] {
      userData.getUserId().toString(),
      userData.getUserId().toString(),
      userData.getUserName(),
      userData.getRealName(),
      userData.getPhone()
    };
    Map<String, String> replaceVar = new HashMap<>();
    final ApiRequestFieldDTO[] userRequestFields = userDataConfigFields.getRequestFields();
    assert(userRequestFields.length == userDataFields.length);
    for(int i = 0; i < userDataFields.length; ++i){
      if(userRequestFields[i].getType().equals("String")){
        replaceVar.put(userRequestFields[i].getName(), "\"" + userDataFields[i] + "\"");
      }
      else{
        replaceVar.put(userRequestFields[i].getName(), userDataFields[i]);
      }
    }
    StringSubstitutor userDataSubstitutor = new StringSubstitutor(replaceVar);
    return userDataSubstitutor.replace(tpl);
  }

  public String generateReservationData(Integer venueId, Reservation reservationData, List<ReservationUserDTO> reservationUsers) throws Exception
  {
    // 填充用户信息
    Optional<ApiConfig> userConfigOptional = apiConfigRepository.findByVenueIdAndType(venueId, ApiType.userinfo);
    String userDataTpl = null;
    if(userConfigOptional.isPresent() && userConfigOptional.get().getOperationType() == ApiOperationType.api){
      userDataTpl = userConfigOptional.get().getRequestContent();
    }
    else{
      final ApiRequestFieldDTO[] userRequestFields = userDataConfigFields.getRequestFields();
      String defaultUserTpl = "{";
      for(int i = 0; i < userRequestFields.length; ++i){
        defaultUserTpl += "\"" + userRequestFields[i].getName() + "\": ${" + userRequestFields[i].getName() + "}";
        if(i < userRequestFields.length - 1){
          defaultUserTpl += ",";
        }
      }
      defaultUserTpl += "}";
      userDataTpl = defaultUserTpl;
    }
    String userRenderStr = "[";
    for(int i = 0; i < reservationUsers.size(); ++i){
      userRenderStr += generateUserData(userDataTpl, reservationUsers.get(i));
      if(i < reservationUsers.size() - 1){
        userRenderStr += ",";
      }
    }
    userRenderStr += "]";
    Optional<ApiConfig> reservationConfigOptional = apiConfigRepository.findByVenueIdAndType(venueId, ApiType.reservation);
    if(reservationConfigOptional.isEmpty()){
      throw new ServiceException(404, "未找到场地管理方配置项");
    }
    String reservationRequestTpl = reservationConfigOptional.get().getRequestContent();
    // 填充预约信息
    CourtAvailability availability = timeslotService.getAvailability(reservationData.getAvailabilityId());
    Court reservationCourt = courtService.getCourtById(availability.getCourtId());
    Timeslot reservationTimeslot = timeslotService.getTimeslotById(availability.getTimeslotId());
    final String[] reservationDataFields = new String[] {
      reservationData.getReservationId().toString(),
      reservationData.getReservationId().toString(),
      userRenderStr,
      reservationCourt.getCourtId().toString(),
      reservationCourt.getCourtId().toString(),
      reservationCourt.getCourtName(),
      reservationTimeslot.getTimeslotId().toString(),
      reservationTimeslot.getTimeslotId().toString(),
      reservationTimeslot.getStartTime().toString(),
      reservationTimeslot.getEndTime().toString(),
      Long.toString(reservationTimeslot.getStartTime().toEpochMilli()),
      Long.toString(reservationTimeslot.getEndTime().toEpochMilli()),
      availability.getPrice().toString(),
      reservationData.getType().toString()
    };
    Map<String, String> replaceVar = new HashMap<>();
    final ApiRequestFieldDTO[] reservationRequestFields = reservationConfigFields.getRequestFields();
    assert(reservationRequestFields.length == reservationDataFields.length);
    for(int i = 0; i < reservationDataFields.length; ++i){
      if(reservationRequestFields[i].getType().equals("String")){
        replaceVar.put(reservationRequestFields[i].getName(), "\"" + reservationDataFields[i] + "\"");
      }
      else{
        replaceVar.put(reservationRequestFields[i].getName(), reservationDataFields[i]);
      }
    }
    StringSubstitutor reservationDataSubstitutor = new StringSubstitutor(replaceVar);
    return reservationDataSubstitutor.replace(reservationRequestTpl);
  }

  public String generateOccupyData(CourtAvailability courtAvailability) throws Exception
  {
    Integer venueId = courtAvailability.getTimeslot().getVenueId();
    Optional<ApiConfig> occupyConfigOptional = apiConfigRepository.findByVenueIdAndType(venueId, ApiType.occupy);
    if(occupyConfigOptional.isEmpty()){
      throw new ServiceException(404, "未找到场地管理方配置项");
    }
    String occupyConfigTpl = occupyConfigOptional.get().getRequestContent();
    Map<String, String> replaceVar = new HashMap<>();
    final String[] occupyDataFields = new String[] {
      courtAvailability.getAvailabilityId().toString(),
      courtAvailability.getAvailabilityId().toString(),
      courtAvailability.getCourtId().toString(),
      courtAvailability.getCourtId().toString(),
      courtAvailability.getCourt().getCourtName(),
      courtAvailability.getTimeslotId().toString(),
      courtAvailability.getTimeslotId().toString(),
      courtAvailability.getTimeslot().getStartTime().toString(),
      courtAvailability.getTimeslot().getEndTime().toString(),
      Long.toString(courtAvailability.getTimeslot().getStartTime().toEpochMilli()),
      Long.toString(courtAvailability.getTimeslot().getEndTime().toEpochMilli())
    };
    final ApiRequestFieldDTO[] occupyRequestFields = occupyConfigFields.getRequestFields();
    assert(occupyDataFields.length == occupyRequestFields.length);
    for(int i = 0; i < occupyRequestFields.length; ++i){
      if(occupyRequestFields[i].getType().equals("String")){
        replaceVar.put(occupyRequestFields[i].getName(), "\"" + occupyDataFields[i] + "\"");
      }
      else{
        replaceVar.put(occupyRequestFields[i].getName(), occupyDataFields[i]);
      }
    }
    StringSubstitutor occupyDataSubstitutor = new StringSubstitutor(replaceVar);
    return occupyDataSubstitutor.replace(occupyConfigTpl);
  }

  public Map<String, String> parseResponse(Integer venueId, String responseData, ApiType type) throws Exception
  {
    Optional<ApiConfig> reservationConfigOptional = apiConfigRepository.findByVenueIdAndType(venueId, type);
    if(reservationConfigOptional.isEmpty()){
      throw new ServiceException(404, "未找到场地管理方配置项");
    }
    String reservationResponseTpl = reservationConfigOptional.get().getResponseContent();
    reservationResponseTpl = reservationResponseTpl.replaceAll("\\s", "");
    Map<String, String> result = new HashMap<>();
    int src = 0;
    int dst = 0;
    while(src < reservationResponseTpl.length() && dst < responseData.length()){
      if(reservationResponseTpl.charAt(src) == responseData.charAt(dst)){
        ++src;
        ++dst;
      }
      else if(src < reservationResponseTpl.length() - 2 && reservationResponseTpl.charAt(src) == '$'){
        if(reservationResponseTpl.charAt(src + 1) != '{'){
          throw new ServiceException(422, "错误的返回请求配置格式");
        }
        int nameEnd = src + 2;
        while(nameEnd < reservationResponseTpl.length() - 2 && reservationResponseTpl.charAt(nameEnd) != '}'){
          ++nameEnd;
        }
        if(reservationResponseTpl.charAt(nameEnd) != '}'){
          throw new ServiceException(422, "错误的返回请求配置格式");
        }
        String fieldName = reservationResponseTpl.substring(src + 2, nameEnd);
        int valueEnd = dst;
        while(valueEnd < responseData.length() && responseData.charAt(valueEnd) != ',' && responseData.charAt(valueEnd) != '}'){
          ++valueEnd;
        }
        String fieldValue = responseData.substring(dst, valueEnd);
        result.put(fieldName, fieldValue);
        src = nameEnd + 1;
        dst = valueEnd;
      }
      else{
        throw new ServiceException(422, "错误的返回请求配置格式");
      }
    }
    return result;
  } 
}
