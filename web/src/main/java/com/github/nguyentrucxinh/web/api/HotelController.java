package com.github.nguyentrucxinh.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.nguyentrucxinh.dto.HotelDto;
import com.github.nguyentrucxinh.helper.exception.CustomNotFoundException;
import com.github.nguyentrucxinh.helper.exception.DTOInControllerNotValidException;
import com.github.nguyentrucxinh.helper.util.Views;
import com.github.nguyentrucxinh.service.HotelService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController implements BaseController<HotelDto, Long> {

    private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//    private static Logger LOGGER = LoggerFactory.getLogger(HotelController.class);

    @Autowired
    private HotelService hotelService;

    @Autowired
    private Environment env;

    @Value("${user.lastname}")
    private String lastname;

    @ApiOperation(value = "${HotelController.findAllPaging.title}", notes = "${HotelController.findAllPaging.notes}")
    @GetMapping("/paging")
    @Override
    public Page<HotelDto> findAll(@PageableDefault(size = 10, page = 0, sort = "name") Pageable pageable) {
        return hotelService.findAll(pageable);
    }

    @ApiOperation(value = "${HotelController.findAll.title}", notes = "${HotelController.findAll.notes}")
    @GetMapping
    @Override
    public List<HotelDto> findAll() {
        LOGGER.debug("Debug log message");
        LOGGER.info("Info log message");
        LOGGER.error("Error log message");
        LOGGER.warn("Warn log message");
        LOGGER.trace("Trace log message");
        return hotelService.findAll();
    }

    @ApiOperation(value = "${HotelController.findById.title}", notes = "${HotelController.findById.notes}")
    @GetMapping("/{id}")
    @Override
    public HotelDto findById(@PathVariable Long id) {
        return hotelService.findById(id);
    }

    @ApiOperation(value = "${HotelController.create.title}", notes = "${HotelController.create.notes}")
    @PostMapping
    @Override
    public Long create(@Validated @RequestBody HotelDto hotelDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new DTOInControllerNotValidException(bindingResult);
        return hotelService.create(hotelDto);
    }

    @ApiOperation(value = "${HotelController.update.title}", notes = "${HotelController.update.notes}")
    @PutMapping("/{id}")
    @Override
    public void update(@PathVariable Long id, @Validated @RequestBody HotelDto hotelDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new DTOInControllerNotValidException(bindingResult);
        hotelService.update(id, hotelDto);
    }

    @ApiOperation(value = "${HotelController.delete.title}", notes = "${HotelController.delete.notes}")
    @DeleteMapping("/{id}")
    @Override
    public void deleteById(@PathVariable Long id) {
        hotelService.deleteById(id);
    }

    @ApiOperation(value = "Get an exception")
    @GetMapping("/exception")
    public void exception() {
        throw new CustomNotFoundException("Not found custom with name is ...");
    }

    @ApiOperation(value = "Get a value from application.properties")
    @GetMapping("/env")
    public String env() {
        return env.getProperty("user.firstname") + " " + lastname;
    }

    /*
     * Customize our JSON request at the API level.
     */

    @PostMapping("/public")
    public HotelDto postPublicView(@JsonView(Views.Public.class) @RequestBody HotelDto hotelDto) {
        return hotelDto;
    }

    @PostMapping("/internal")
    public HotelDto postInternalView(@JsonView(Views.Internal.class) @RequestBody HotelDto hotelDto) {
        return hotelDto;
    }

    /*
     * customize our JSON response at the API level.
     */

    @JsonView(Views.Public.class)
    @GetMapping("/public")
    public HotelDto postPublicView() {
        return hotelService.findById(1L);
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/internal")
    public HotelDto postInternalView() {
        return hotelService.findById(1L);
    }

    @GetMapping("/sample-response-entity")
    public ResponseEntity sampleResponseEntity() {
        // return ResponseEntity.badRequest().body(null);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "${HotelController.sampleSwagger.title}", notes = "${HotelController.sampleSwagger.notes}")
    @ApiImplicitParams(@ApiImplicitParam(name = "X-Authorization",
            value = "Basic auth_credentials",
            required = true,
            dataType = "string",
            paramType = "header"))
    @GetMapping("/sample-swagger/{companyId}")
    public ResponseEntity<Page<HotelDto>> sampleSwagger(
            @ApiParam(value = "The primary key of Company") @PathVariable Long companyId, Pageable pageable) {
        return null;

//        Ignore properties model in api docs
//        @ApiModelProperty(hidden = true)
    }
}
