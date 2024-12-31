package com.tongji.sportmanagement.GroupSubsystem.Controller;

import com.tongji.sportmanagement.Common.DTO.ChatDTO;
import com.tongji.sportmanagement.Common.DTO.LittleUserDTO;
import com.tongji.sportmanagement.Common.DTO.ResultData;
import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.GroupSubsystem.DTO.CompleteGroupDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupApplicationDTO;
import com.tongji.sportmanagement.GroupSubsystem.DTO.GroupDeleteDTO;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupApplicationService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupService;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupApplicationService groupApplicationService;

    public GroupController(GroupService groupService, GroupApplicationService groupApplicationService) {
        this.groupService = groupService;
        this.groupApplicationService = groupApplicationService;
    }

    @PostMapping("")
    public ResponseEntity<Object> createGroup(@RequestBody CompleteGroupDTO completeGroup) {
        try {
            //验证token
            groupService.createGroup(completeGroup);
            return ResponseEntity.status(200).body(ResultMsg.success("团体已经成功创建"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<Object> getGroups() {
        try{
            var groups=groupService.getGroups();
            return ResponseEntity.status(200).body(ResultData.success(groups));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Object> getGroupByID(@PathVariable Integer groupId) {
        try{
            var group=groupService.getGroupDetail(groupId);
            return ResponseEntity.status(200).body(ResultData.success(group));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteGroup(@RequestBody GroupDeleteDTO groupDeleteDTO) {
        try{
            groupService.deleteGroup(groupDeleteDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("成功解散团体"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/application")
    public ResponseEntity<Object> getGroupApplication(Integer userId) {
        try{
            var applications= groupApplicationService.getGroupApplications(userId);
            return ResponseEntity.status(200).body(ResultData.success(applications));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PostMapping("/application")
    public ResponseEntity<Object> sendGroupApplication(@RequestBody GroupApplicationDTO groupApplicationDTO) {
        try{
            groupApplicationService.sendApplicationIng(groupApplicationDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("已经发送团体加入申请"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PatchMapping("/application")
    public ResponseEntity<Object> updateGroupApplication(@RequestBody AuditResultDTO auditResultDTO) {
        try{
            groupApplicationService.updateApplication(auditResultDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("已经成功处理申请"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    public List<LittleUserDTO> getUserDetail(List<Integer> users){
        return null;
    }

    public Integer createGroupChat(ChatDTO chatDTO){
        return null;
    }
/*

    @DeleteMapping("/removeGroupMember")
    public ResponseEntity<ResultMsg> removeGroupMember(String groupID,String operatorID,String userID) {
        return ResponseEntity.status(200).body(new ResultMsg(groupID+operatorID+userID));
    }

    @GetMapping("/getGroupMembers")
    public ResponseEntity<ArrayList<ExpandGroupMember>> getGroupMembers(String groupID) {
        var p= new ArrayList<ExpandGroupMember>();
        p.add(new ExpandGroupMember(groupID,null,null,null));
        return ResponseEntity.status(200).body(p);
    }

    @PostMapping("/postGroupApplication")
    public ResponseEntity<ResultMsg> postGroupApplication(@RequestBody GroupApplication groupApplication) {
       return ResponseEntity.status(200).body(new ResultMsg(groupApplication.toString()));
    }

    @PutMapping("/putGroupApplication")
    public ResponseEntity<ResultMsg> putGroupApplication(String applicationID,boolean result) {
        return ResponseEntity.status(200).body(new ResultMsg(applicationID+result));
    }

    @GetMapping("/getGroupMemberRecord")
    public ResponseEntity<ArrayList<GroupMemberRecord>> getGroupMemberRecord(String userID) {
        var p= new ArrayList<GroupMemberRecord>();
        p.add(new GroupMemberRecord(userID,null,null));
        return ResponseEntity.status(200).body(p);
    }*/
}
