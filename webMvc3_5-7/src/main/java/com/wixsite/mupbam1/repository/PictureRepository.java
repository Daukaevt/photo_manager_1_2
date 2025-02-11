package com.wixsite.mupbam1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wixsite.mupbam1.models.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
}
