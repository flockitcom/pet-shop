package com.zq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import com.zq.bean.PetDo;
import com.zq.bean.PetTypeDo;
import com.zq.error.BusinessException;
import com.zq.response.CommonReturnType;
import com.zq.service.FileService;
import com.zq.service.impl.PetServiceImpl;
import com.zq.service.model.PetModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 86132 on 2020/01/28.
 */
@Api(value = "宠物模块模块", description = "宠物模块")
@RestController
@RequestMapping("pet")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class PetController extends BaseController {

    @Autowired
    private PetServiceImpl petService;

    @Autowired
    private FileService fileUploadService;

    @ApiOperation(value = "根据Id查询宠物")
    @PostMapping(value = "/getPet")
    public CommonReturnType getPet(@ApiParam(name = "id",required = true) @RequestParam Integer id) throws BusinessException, JsonProcessingException {
        PetModel petModel = petService.getPetDoById(id);
        return CommonReturnType.createSuccess(petModel);
    }

    @ApiOperation(value = "根据不同条件查询宠物")
    @PostMapping(value = "/getPetByCondition")
    public CommonReturnType getPetByCondition(@ApiParam(name = "pageNo",value = "分页页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                              @ApiParam(name = "pageSize",value = "每页条数") @RequestParam(required = false,defaultValue = "12") Integer pageSize,
                                              @ApiParam(name = "keyword",value = "关键词") @RequestParam(required = false) String keyword,
                                              @ApiParam(name = "typeId",value = "宠物种类") @RequestParam(required = false) Integer typeId) throws UnsupportedEncodingException, JsonProcessingException {
//        if (!StringUtils.isEmpty(keyword)){
//            keyword = new String(keyword.getBytes("ISO-8859-1"));
//        }
        PageInfo<PetDo> petListPageInfo = petService.getPetList(keyword, typeId, pageNo, pageSize);
        return CommonReturnType.createSuccess(petListPageInfo);
    }

    @ApiOperation(value = "删除宠物")
    @PostMapping(value = "/deletePet")
    public CommonReturnType deletePet(@ApiParam(name = "id",required = true) @RequestParam Integer id) throws BusinessException{
        petService.deletePet(id);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "文件上传")
    @PostMapping(value = "/imgUpload")
    public CommonReturnType imgUpload(@RequestParam(value = "file") MultipartFile file) throws BusinessException{
        String petImg = fileUploadService.fileUpload(file).get(0);
        return CommonReturnType.createSuccess(petImg);
    }

    @ApiOperation(value = "跟新宠物信息")
    @PostMapping(value = "/updatePet")
    public CommonReturnType updatePet(@ApiParam PetDo petDo,
                                      @ApiParam(name = "imgUrlListStr",value = "多图片url List集合",required = true) @RequestParam String imgUrlListStr) throws BusinessException {
        petService.updatePet(petDo,imgUrlListStr);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "发布商品")
    @PostMapping(value = "/createPet")
    public CommonReturnType createPet(@ApiParam PetDo petDo,
                                      @ApiParam(name = "imgUrlListStr",value = "多图片url List集合",required = true) @RequestParam String imgUrlListStr) throws BusinessException {
        petService.create(petDo,imgUrlListStr);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "发布商品分类")
    @PostMapping(value = "/createPetType")
    public CommonReturnType createPetType(@ApiParam PetTypeDo petTypeDo) throws BusinessException {
        petService.createType(petTypeDo);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "获取宠物种类")
    @GetMapping(value = "/getPetType")
    public CommonReturnType getPetType() throws BusinessException {
        List<PetTypeDo> petTypeList = petService.getPetTypeList();
        return CommonReturnType.createSuccess(petTypeList);
    }

    @ApiOperation(value = "根据id查询宠物种类")
    @PostMapping(value = "/getPetTypeById")
    public CommonReturnType getPetTypeById(@ApiParam(name = "id",required = true) @RequestParam Integer id) throws BusinessException{
        PetTypeDo petType = petService.getPetTypeById(id);
        return CommonReturnType.createSuccess(petType);
    }

    @ApiOperation(value = "分页获取宠物种类")
    @PostMapping(value = "/getPetTypeByPage")
    public CommonReturnType getPetTypeByPage(@ApiParam(name = "pageNo",value = "分页页码") @RequestParam(required = false,defaultValue = "1") Integer pageNo,
                                             @ApiParam(name = "pageSize",value = "每页条数") @RequestParam(required = false,defaultValue = "6") Integer pageSize) throws BusinessException{
        PageInfo<PetTypeDo> petTypeDoPageInfo = petService.getPetTypeByPage(pageNo,pageSize);
        return CommonReturnType.createSuccess(petTypeDoPageInfo);
    }

    @ApiOperation(value = "根据id删除宠物种类")
    @PostMapping(value = "/deletePetTypeById")
    public CommonReturnType deletePetTypeById(@ApiParam(name = "id",required = true) @RequestParam Integer id) throws BusinessException{
        petService.deletePetTypeById(id);
        return CommonReturnType.createSuccess(null);
    }

    @ApiOperation(value = "根据id修改宠物种类")
    @PostMapping(value = "/updatePetTypeById")
    public CommonReturnType updatePetTypeById(@ApiParam PetTypeDo petTypeDo) throws BusinessException{
        petService.updatePetTypeById(petTypeDo);
        return CommonReturnType.createSuccess(null);
    }
}
