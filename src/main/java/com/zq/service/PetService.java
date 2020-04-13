package com.zq.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.zq.bean.PetDo;
import com.zq.bean.PetTypeDo;
import com.zq.error.BusinessException;
import com.zq.service.model.PetModel;

import java.util.List;

/**
 * Created by 86132 on 2020/01/28.
 */
public interface PetService {

    PetModel getPetDoById(Integer id) throws BusinessException, JsonProcessingException;

    List<String> getImgListByPetId(Integer petId) throws BusinessException;

    List<PetTypeDo> getPetTypeList() throws BusinessException;

    PageInfo<PetTypeDo> getPetTypeByPage(Integer pageNo, Integer pageSize);

    PageInfo<PetDo> getPetList(String keyword, Integer typeId, Integer pageNo, Integer pageSize) throws JsonProcessingException;

    //库存扣减
    boolean decreaseStock(Integer petId,Integer amount);

    //商品销量增加
    void increaseSales(Integer petId,Integer amount);

    void deletePet(Integer id) throws BusinessException;

    void updatePet(PetDo petDo, String imgUrlListStr) throws BusinessException;

    void create(PetDo petDo, String imgUrlListStr) throws BusinessException;

    void createType(PetTypeDo petTypeDo) throws BusinessException;

    PetTypeDo getPetTypeById(Integer id) throws BusinessException;

    void deletePetTypeById(Integer id) throws BusinessException;

    void updatePetTypeById(PetTypeDo petTypeDo) throws BusinessException;
}
