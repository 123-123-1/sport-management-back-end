package com.tongji.sportmanagement.VenueSubsystem.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tongji.sportmanagement.VenueSubsystem.DTO.CommentItemReflection;
import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;

public interface CommentRepositiory extends JpaRepository<VenueComment, Integer>
{
  // @Query(value = "SELECT * FROM venue_comment WHERE venue_id = :venueId ORDER BY time DESC LIMIT :count OFFSET :offset", nativeQuery = true)
  // List<VenueComment> findCommentByVenueId(@Param("venueId") Integer venueId, @Param("offset") long offset, @Param("count") int count);

  // @Query(value = "SELECT COUNT(*) FROM venue_comment WHERE venue_id = :venueId", nativeQuery = true)
  // long getCommentCount(@Param("venueId") Integer venueId);

  @Query(
    value = "SELECT " + //
            "    vc.comment_id, " + //
            "    vc.content, " + //
            "    vc.time, " + //
            "    vc.venue_id, " + //
            "    u.user_id, " + //
            "    vc.score, " + //
            "    u.user_name " + //
            "FROM venue_comment vc " + //
            "JOIN `user` u ON vc.user_id = u.user_id " + //
            "WHERE venue_id = :venueId",
    nativeQuery = true,
    countQuery = "SELECT COUNT(*) FROM venue_comment vc WHERE vc.venue_id = :venueId"
  )
  Page<CommentItemReflection> getVenueComments(@Param("venueId") Integer venueId, Pageable pageable);
}
