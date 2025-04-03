package com.tongji.sportmanagement.ReservationSubsystem.Repository;

import org.springframework.data.jpa.domain.Specification;

import com.tongji.sportmanagement.AccountSubsystem.Entity.User;
import com.tongji.sportmanagement.GroupSubsystem.Entity.Group;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.GroupReservation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.Reservation;
import com.tongji.sportmanagement.ReservationSubsystem.Entity.UserReservation;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Court;
import com.tongji.sportmanagement.VenueSubsystem.Entity.CourtAvailability;
import com.tongji.sportmanagement.VenueSubsystem.Entity.Timeslot;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.ListJoin;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

// 用于动态构建Reservation查询条件
public class ReservationSpecification
{
  // 通过场地ID查询
  // public static Specification<Reservation> venueBaseQuery(Integer venueId) {
  //   return (root, query, cb) -> {
  //     // 1. 声明 JOIN 关系
  //     Join<Reservation, CourtAvailability> caJoin = root.join("courtAvailability");
  //     Join<CourtAvailability, Court> courtJoin = caJoin.join("court");
  //     Join<CourtAvailability, Timeslot> timeslotJoin = caJoin.join("timeslot");
      
  //     // 2. 处理多对多关联（LEFT JOIN GroupReservation 和 Group）
  //     ListJoin<Reservation, GroupReservation> grJoin = root.joinList(
  //         "groupReservations", JoinType.LEFT
  //     );
  //     Join<GroupReservation, Group> groupJoin = grJoin.join("group", JoinType.LEFT);

  //     // 3. 显式指定 SELECT 字段和聚合方式
  //     query.multiselect(
  //       root.get("id").alias("reservationId"),
  //       courtJoin.get("name").alias("courtName"),
  //       timeslotJoin.get("startTime").alias("startTime"),
  //       timeslotJoin.get("endTime").alias("endTime"),
  //       cb.function("JSON_ARRAYAGG", String.class, groupJoin.get("name"))
  //     );

  //     // 4. 分组条件（确保聚合字段正确）
  //     query.groupBy(
  //       root.get("id"), 
  //       courtJoin.get("name"),
  //       timeslotJoin.get("startTime"),
  //       timeslotJoin.get("endTime")
  //     );

  //     // 5. WHERE 条件
  //     return cb.equal(courtJoin.get("venueId"), venueId);
  //   };
  // }

    // 用户过滤条件
    public static Specification<Reservation> filterByUser(Integer userId, String userName) {
      return (root, query, cb) -> {
        if (userId == null && userName == null) {
            return cb.conjunction(); // 无过滤条件
        }
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<UserReservation> ur = subquery.from(UserReservation.class);
        Join<UserReservation, User> u = ur.join("user");
        subquery.select(ur.get("reservation").get("id"))
          .where(
            cb.and(
              cb.equal(ur.get("reservation"), root), // 关联主查询的 Reservation
              cb.or(
                  userId != null ? cb.equal(u.get("id"), userId) : cb.conjunction(),
                  userName != null ? cb.equal(u.get("userName"), userName) : cb.conjunction()
              )
            )
          );

        return cb.exists(subquery);
      };
    }
}
