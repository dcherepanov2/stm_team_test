package net.proselyte.jwtappdemo.repository;

import net.proselyte.jwtappdemo.model.Comics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ComicsRepo extends JpaRepository<Comics,Integer>, JpaSpecificationExecutor<Comics> {
    Comics findCharacterBySlug(String slug);

    Comics findComicsBySlug(String slug);

}
