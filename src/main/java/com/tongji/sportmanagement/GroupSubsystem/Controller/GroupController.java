package com.tongji.sportmanagement.GroupSubsystem.Controller;

import com.tongji.sportmanagement.Common.DTO.ResultMsg;
import com.tongji.sportmanagement.GroupSubsystem.DTO.*;
import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupApplicationService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupMemberService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupRecordService;
import com.tongji.sportmanagement.GroupSubsystem.Service.GroupService;
import com.tongji.sportmanagement.Common.DTO.AuditResultDTO;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
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
    public ResponseEntity<ResultMsg> createGroup(@RequestAttribute int idFromToken, @RequestBody CompleteGroupDTO completeGroup) {
        return ResponseEntity.ok().body(groupService.createGroup(completeGroup, idFromToken));
    }

    @GetMapping("")
    public ResponseEntity<List<Group>> getGroups() {
        return ResponseEntity.ok().body(groupService.getGroups());
    }

    @GetMapping("/byUser")
    public ResponseEntity<List<Group>> getGroupByUser(@RequestAttribute Integer idFromToken) throws Exception {
        return ResponseEntity.ok().body(groupService.getByUserId(idFromToken));
    }

    @GetMapping("/leadergroups")
    public ResponseEntity<List<Group>> getUserLeaderGroup(@RequestAttribute Integer idFromToken) throws Exception {
        return ResponseEntity.ok().body(groupService.getByUserIdFiltered(idFromToken));
    }

    @GetMapping("/byId/{groupId}")
    public ResponseEntity<GroupDetailDTO> getGroupByID(@PathVariable Integer groupId) {
        return ResponseEntity.ok().body(groupService.getGroupDetail(groupId));
    }

    @GetMapping("/byName/{groupName}")
    public ResponseEntity<List<Group>> getGroupByName(@PathVariable String groupName) {
        return ResponseEntity.ok().body(groupService.getGroupByName(groupName));
    }

    @DeleteMapping("")
    public ResponseEntity<ResultMsg> deleteGroup(@RequestAttribute int idFromToken ,Integer groupId) {
        return ResponseEntity.ok().body(groupService.deleteGroup(groupId, idFromToken));
    }

    @GetMapping("/application")
    public ResponseEntity<List<GroupApplicationResultDTO>> getGroupApplication(@RequestAttribute  int idFromToken) {
        return ResponseEntity.ok().body(groupApplicationService.getGroupApplications(idFromToken));
    }

    @PostMapping("/application")
    public ResponseEntity<ResultMsg> sendGroupApplication(@RequestAttribute int idFromToken, @RequestBody GroupApplicationDTO groupApplicationDTO) {
        return ResponseEntity.ok().body(groupApplicationService.sendApplicationIng(groupApplicationDTO, idFromToken));
    }

    @PostMapping("/application/by")
    public ResponseEntity<ResultMsg> inviteMember(@RequestAttribute int idFromToken, @RequestBody InviteGroupDTO inviteDTO) {
        return ResponseEntity.ok().body(groupApplicationService.inviteMember(inviteDTO, idFromToken));
    }

    @PatchMapping("/application")
    public ResponseEntity<ResultMsg> updateGroupApplication(@RequestAttribute int idFromToken, @RequestBody AuditResultDTO auditResultDTO) {
        return ResponseEntity.ok().body(groupApplicationService.updateApplication(auditResultDTO, idFromToken));
    }

    @DeleteMapping("/members")
    public ResponseEntity<ResultMsg> deleteGroupMember(@RequestAttribute int idFromToken, Integer groupId) {
        return ResponseEntity.ok().body(groupMemberService.quitGroup(idFromToken,groupId));
    }

    @DeleteMapping("/members/by")
    public ResponseEntity<Object> removeGroupMember(@RequestAttribute int idFromToken, @RequestParam Integer groupId, @RequestParam Integer memberId) {
        return ResponseEntity.ok().body( groupMemberService.dropMember(groupId, memberId, idFromToken));
    }

    @PatchMapping("/members")
    public ResponseEntity<Object> setGroupMemberRole(@RequestAttribute int idFromToken, @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.ok().body(groupMemberService.setRole(roleDTO, idFromToken));
    }

    @GetMapping("/records")
    public ResponseEntity<Object> getGroupRecords(Integer groupId,Integer targetId,@RequestAttribute int idFromToken) {
        return ResponseEntity.ok().body(groupRecordService.getRecord(idFromToken, targetId, groupId));
    }

}
