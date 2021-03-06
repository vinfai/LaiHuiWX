package com.lhpc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lhpc.model.Stroke;

public interface StrokeMapper {
    int deleteByPrimaryKey(Integer strokeId);

    int insert(Stroke record);

    int insertSelective(Stroke record);

    Stroke selectByPrimaryKey(Integer strokeId);

    int updateByPrimaryKeySelective(Stroke record);

    int updateByPrimaryKey(Stroke record);
    
    List<Stroke> selectByUserIdAndIsEnable(@Param("userId") int userId, @Param("isEnable") int isEnable);
    
    List<Stroke> selectCrossCityList(Stroke stroke);
    
    List<Stroke> selectPersonalItineraryList(Stroke stroke);
    
    int selectCrossCityCount(Stroke stroke);
    
    int selectCount(Stroke stroke);

    List<Stroke> selectSearchStrokeList(Stroke stroke);

	int update4AccessCount(@Param("strokeId")Integer strokeId);

	int update4Seats(@Param("strokeId")Integer strokeId,@Param("seats")Integer seats);
}