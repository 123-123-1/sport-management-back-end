package com.tongji.sportmanagement.ExternalManagementSubsystem.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tongji.sportmanagement.Common.ServiceException;
import com.tongji.sportmanagement.Common.SportManagementUtils;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigCreateDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.DTO.ApiConfigResponseDTO;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiConfig;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiOperationType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Entity.ApiType;
import com.tongji.sportmanagement.ExternalManagementSubsystem.Repository.ApiConfigRepository;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Venue;
import com.tongji.sportmanagement.VenueSubsystem.Repository.VenueRepository;

import jakarta.transaction.Transactional;

@Service
public class ApiConfigService
{
  
  @Autowired
  private VenueRepository venueRepository;
  @Autowired
  private ApiConfigRepository apiConfigRepository;

  public Integer getVenueIdByManager(Integer managerId) throws Exception
  {
    Optional<Venue> venueOptional = venueRepository.findByManagerId(managerId);
    if(venueOptional.isEmpty()){
      throw new ServiceException(404, "未找到管理的场馆");
    }
    return venueOptional.get().getVenueId();
  }

  public ApiConfigResponseDTO getConfigByManager(ApiType type, Integer managerId) throws Exception
  {
    if(type == null){
      throw new ServiceException(400, "API类型参数错误");
    }
    Integer venueId = getVenueIdByManager(managerId);
    Optional<ApiConfig> configOptional = apiConfigRepository.findByVenueIdAndType(venueId, type);
    return new ApiConfigResponseDTO(configOptional.isPresent() ? 1 : 0, configOptional);
  }

  public ApiConfig createApiConfig(ApiConfigCreateDTO createInfo, Integer managerId) throws Exception
  {
    if(createInfo.getType() == null){
      throw new ServiceException(400, "API类型参数错误");
    }
    Integer venueId = getVenueIdByManager(managerId);
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
}
