package com.tongji.sportmanagement.VenueSubsystem.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;

public interface CommentRepositiory extends CrudRepository<VenueComment, Integer>
{
  @Query(value = "SELECT * FROM venue_comment WHERE venue_id = :venueId LIMIT :count OFFSET :offset", nativeQuery = true)
  Iterable<VenueComment> findCommentByVenueId(@Param("venueId") Integer venueId, @Param("offset") long offset, @Param("count") int count);

  @Query(value = "SELECT COUNT(*) FROM venue_comment WHERE venue_id = :venueId", nativeQuery = true)
  long getCommentCount(@Param("venueId") Integer venueId);
}
