package com.tongji.sportmanagement.GroupSubsystem.Controller;

import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.GroupSubsystem.DTO.*;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupApplicationService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupMemberService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupRecordService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupService;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final GroupApplicationService groupApplicationService;
    private final GroupMemberService groupMemberService;
    private final GroupRecordService groupRecordService;

    public GroupController(GroupService groupService, GroupApplicationService groupApplicationService, GroupMemberService groupMemberService, GroupRecordService groupRecordService) {
        this.groupService = groupService;
        this.groupApplicationService = groupApplicationService;
        this.groupMemberService = groupMemberService;
        this.groupRecordService = groupRecordService;
    }

    @PostMapping("")
    public ResponseEntity<Object> createGroup(@RequestAttribute Integer idFromToken, @RequestBody CompleteGroupDTO completeGroup) {
        try {
            completeGroup.setCreatorId(idFromToken);
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
            return ResponseEntity.status(200).body(groups);
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Object> getGroupByID(@PathVariable Integer groupId) {
        try{
            var group=groupService.getGroupDetail(groupId);
            return ResponseEntity.status(200).body(group);
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/{groupName}")
    public ResponseEntity<Object> getGroupByName(@PathVariable String groupName) {
        try{
            var group=groupService.getGroupByName(groupName);
            return ResponseEntity.status(200).body(group);
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Object> deleteGroup(@RequestAttribute Integer idFromToken ,Integer groupId) {
        try{
            groupService.deleteGroup(groupId,idFromToken);
            return ResponseEntity.status(200).body(ResultMsg.success("成功解散团体"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/application")
    public ResponseEntity<Object> getGroupApplication(@RequestAttribute  Integer idFromToken) {
        try{
            var applications= groupApplicationService.getGroupApplications(idFromToken);
            return ResponseEntity.status(200).body(applications);
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PostMapping("/application")
    public ResponseEntity<Object> sendGroupApplication(@RequestAttribute Integer idFromToken, @RequestBody GroupApplicationDTO groupApplicationDTO) {
        try{
            groupApplicationDTO.setApplicantId(idFromToken);
            groupApplicationService.sendApplicationIng(groupApplicationDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("已经发送团体加入申请"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PostMapping("/application/{userId}")
    public ResponseEntity<Object> inviteMember(@PathVariable Integer userId, @RequestBody InviteGroupDTO inviteDTO) {
        try{
            groupApplicationService.inviteMember(inviteDTO,userId);
            return ResponseEntity.status(200).body(ResultMsg.success("已经向该用户发送邀请"));
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

    @DeleteMapping("/members")
    public ResponseEntity<Object> deleteGroupMember(@RequestBody MemberQuitDTO quitDTO) {
        try{
            groupMemberService.quitGroup(quitDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("退出团体成功"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @DeleteMapping("/members/by")
    public ResponseEntity<Object> removeGroupMember(@RequestBody MemberDropDTO dropDTO) {
        try{
            groupMemberService.dropMember(dropDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("将用户移出团体成功"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @PatchMapping("/members")
    public ResponseEntity<Object> setGroupMemberRole(@RequestBody RoleDTO roleDTO) {
        try{
            groupMemberService.setRole(roleDTO);
            return ResponseEntity.status(200).body(ResultMsg.success("已成功设置用户权限"));
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }

    @GetMapping("/records")
    public ResponseEntity<Object> getGroupRecords(Integer groupId,Integer targetId,Integer operatorId) {
        try{
            var records=groupRecordService.getRecord(operatorId,targetId,groupId);
            return ResponseEntity.status(200).body(records);
        }
        catch (Exception e) {
            return  ResponseEntity.status(500).body(ResultMsg.error(e.getMessage()));
        }
    }




}
