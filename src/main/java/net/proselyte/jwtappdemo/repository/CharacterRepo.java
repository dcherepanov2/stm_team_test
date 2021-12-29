package net.proselyte.jwtappdemo.repository;

import net.proselyte.jwtappdemo.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CharacterRepo extends JpaRepository<Character,Integer>, JpaSpecificationExecutor<Character> {

    Character findCharacterBySlug(String slug);

}
