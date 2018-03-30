package com.kxxfydj.mapper;

import com.kxxfydj.entity.CodeRepository;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by kxxfydj on 2018/3/10.
 */
public interface CodeRepositoryMapper {
    List<CodeRepository> selectAll();
    CodeRepository selectByName(String repositoryName);
    CodeRepository selectByNameAndCondition(@Param("repositoryName") String repositoryName, @Param("condition") String condition);
    void insert(CodeRepository codeRepository);
    int updateCodeRepository(CodeRepository codeRepository);
}
