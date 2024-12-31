package com.tongji.sportmanagement.GroupSubsystem.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

  /*  @PostMapping("/")
    public ResponseEntity<Object> createGroup(@RequestBody CompleteGroup completeGroup) {

    }

    @GetMapping("/getGroups")
    public ResponseEntity<Group> getGroups() {
        return ResponseEntity.status(200).body(new Group("10000",null,null,null,null));
    }

    @GetMapping("/getGroupByID")
    public ResponseEntity<Group> getGroupByID(String groupID) {
        return ResponseEntity.status(200).body(new Group(groupID,null,null,null,null));
    }

    @DeleteMapping("/deleteGroup")
    public ResponseEntity<ResultMsg> deleteGroup(String groupID,String userID) {
        return ResponseEntity.status(200).body(new ResultMsg(groupID+userID));
    }

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
