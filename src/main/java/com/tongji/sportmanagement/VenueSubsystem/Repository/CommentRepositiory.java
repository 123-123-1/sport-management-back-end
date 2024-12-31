package com.tongji.sportmanagement.VenueSubsystem.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tongji.sportmanagement.VenueSubsystem.Entity.VenueComment;

public interface CommentRepositiory extends CrudRepository<VenueComment, Integer>
{
  @Query
  Iterable<VenueComment> findAllByVenueId(Integer venueId);
}
