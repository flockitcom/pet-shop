package com.zq.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zq.bean.PetDo;
import com.zq.bean.PetImgDo;
import com.zq.bean.PetTypeDo;
import com.zq.dao.OrderDoMapper;
import com.zq.dao.PetDoMapper;
import com.zq.dao.PetImgDoMapper;
import com.zq.dao.PetTypeDoMapper;
import com.zq.error.BusinessException;
import com.zq.error.EmBusinessError;
import com.zq.service.PetService;
import com.zq.service.model.PetModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 86132 on 2020/01/28.
 */
@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetDoMapper petDoMapper;

    @Autowired
    private PetImgDoMapper petImgDoMapper;

    @Autowired
    private PetTypeDoMapper petTypeDoMapper;

    @Override
    public PetModel getPetDoById(Integer id) throws BusinessException, JsonProcessingException {
        PetDo petDo = petDoMapper.selectByPrimaryKey(id);
        if (petDo == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<String> imgList = getImgListByPetId(id);
        PetModel petModel = convertPetModelFromPetDo(petDo);
        petModel.setImgs(imgList);
        return petModel;
    }

    @Override
    public List<String> getImgListByPetId(Integer petId) throws BusinessException {
        List<PetImgDo> petImgDoList = petImgDoMapper.selectByPetId(petId);
        if (petImgDoList == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        List<String> imgs = new ArrayList<>();
        for (PetImgDo petImgDo : petImgDoList) {
            imgs.add(petImgDo.getImg());
        }
        return imgs;
    }

    @Override
    public List<PetTypeDo> getPetTypeList() throws BusinessException {
        List<PetTypeDo> petTypeDoList = petTypeDoMapper.selectAll();
        return petTypeDoList;
    }

    @Override
    public PageInfo<PetTypeDo> getPetTypeByPage(Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<PetTypeDo> petTypeDoList = petTypeDoMapper.selectAll();
        PageInfo<PetTypeDo> petTypeDoPageInfo = new PageInfo<>(petTypeDoList);
        return petTypeDoPageInfo;
    }

    @Override
    public PageInfo<PetDo> getPetList(String keyword, Integer typeId, Integer pageNo, Integer pageSize) throws JsonProcessingException {
        if (StringUtils.isEmpty(keyword) || "null".equals(keyword) || "undefined".equals(keyword)) {
            keyword = null;
        }
        PageHelper.startPage(pageNo, pageSize);
        List<PetDo> petDoList;
        if (!StringUtils.isEmpty(keyword) && typeId == null) {
            petDoList = petDoMapper.selectPetListByKeyword(keyword);
        } else if (typeId != null && StringUtils.isEmpty(keyword)) {
            petDoList = petDoMapper.selectPetListByTypeId(typeId);
        } else {
            petDoList = petDoMapper.selectPetList();
        }
        PageInfo petPageInfo = new PageInfo(petDoList);
        List<PetModel> petModelList = new ArrayList<>();
        for (PetDo petDo : petDoList) {
            PetTypeDo petTypeDo = petTypeDoMapper.selectByPrimaryKey(petDo.getTypeId());
            PetModel petModel = convertPetModelFromPetDo(petDo);
            petModel.setTypeStr(petTypeDo.getShopping());
            petModelList.add(petModel);
        }
        petPageInfo.setList(petModelList);
        return petPageInfo;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer petId, Integer amount) {
        int affectedRow = petDoMapper.decreaseStock(petId, amount);
        if (affectedRow > 0) {
            //跟新库存成功
            return true;
        } else {
            //跟新库存失败
            return false;
        }
    }

    @Override
    @Transactional
    public void increaseSales(Integer petId, Integer amount) {
        petDoMapper.increaseSales(petId, amount);
    }

    @Override
    @Transactional
    public void deletePet(Integer id) throws BusinessException {
        int row = petDoMapper.deleteByPrimaryKey(id);
        if (row == 0) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @Override
    @Transactional
    public void updatePet(PetDo petDo, String imgUrlListStr) throws BusinessException {
        if (petDo == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        String[] split = imgUrlListStr.split(",");
        //设置宠物信息主图，split[0]为主图
        petDo.setMainimgUrl(split[0]);
        try {
            petDoMapper.updateByPrimaryKeySelective(petDo);
        } catch (Exception e) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        //删除宠物信息图片
        petImgDoMapper.deleteByPrimaryKey(petDo.getId());
        PetImgDo petImgDo = new PetImgDo();
        if (1!=split.length){
            petImgDoMapper.deleteByPetId(petDo.getId());
            for (int i = 1; i < split.length; i++) {
                petImgDo.setPetId(petDo.getId());
                petImgDo.setImg(split[i]);
                petImgDoMapper.insertSelective(petImgDo);
            }
        }
    }

    @Override
    @Transactional
    public void create(PetDo petDo, String imgUrlListStr) throws BusinessException {
        if (petDo == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        String[] split = imgUrlListStr.split(",");
        //设置宠物信息主图，split[0]为主图
        petDo.setMainimgUrl(split[0]);
        petDoMapper.insertSelective(petDo);
        PetImgDo petImgDo = new PetImgDo();
        for (int i = 1; i < split.length; i++) {
            petImgDo.setPetId(petDo.getId());
            petImgDo.setImg(split[i]);
            petImgDoMapper.insertSelective(petImgDo);
        }

    }

    @Override
    @Transactional
    public void createType(PetTypeDo petTypeDo) throws BusinessException {
        if (petTypeDo == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        petTypeDoMapper.insertSelective(petTypeDo);
    }

    @Override
    public PetTypeDo getPetTypeById(Integer id) throws BusinessException {
        PetTypeDo petTypeDo = petTypeDoMapper.selectByPrimaryKey(id);
        if (petTypeDo == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        return petTypeDo;
    }

    @Override
    @Transactional
    public void deletePetTypeById(Integer id) throws BusinessException {
        getPetTypeById(id);
        List<PetDo> petDoList = petDoMapper.selectPetListByTypeId(id);
        if (petDoList.size() != 0) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "该分类下存在商品，无法删除");
        }
        petTypeDoMapper.deleteByPrimaryKey(id);
    }

    @Override
    @Transactional
    public void updatePetTypeById(PetTypeDo petTypeDo) throws BusinessException {
        if (petTypeDo == null || petTypeDo.getId() == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        PetTypeDo petTypeDoNew = petTypeDoMapper.selectByPrimaryKey(petTypeDo.getId());
        if (petTypeDoNew == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        int row = petTypeDoMapper.updateByPrimaryKeySelective(petTypeDo);
        if (row == 0){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"修改信息失败");
        }
    }

    //pet 转为model
    private PetModel convertPetModelFromPetDo(PetDo petDo) throws JsonProcessingException {
        if (petDo == null) {
            return null;
        }
        PetModel petModel = new PetModel();
        BeanUtils.copyProperties(petDo, petModel);
        return petModel;
    }
}
