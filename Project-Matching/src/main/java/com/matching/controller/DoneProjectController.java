package com.matching.controller;

import com.matching.domain.DoneProject;
import com.matching.domain.docs.RestDocs;
import com.matching.domain.dto.DoneProjectDTO;
import com.matching.service.DoneProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.net.URI;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/doneproject")
public class DoneProjectController {

    @Autowired
    private DoneProjectService doneProjectService;

    @GetMapping(value = "/{idx}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoneProject(@PathVariable Long idx) {

        if(doneProjectService.findByDoneProject(idx))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Resource<?> resource = doneProjectService.getDoneProject(idx);
        resource.add(linkTo(methodOn(DoneProjectController.class).getDoneProject(idx)).withSelfRel());
        resource.add(linkTo(methodOn(DoneProjectController.class).getDoneProjectUsedSkills(idx)).withRel("Used Skills"));

        URI uri = linkTo(methodOn(DoneProjectController.class).getDoneProject(idx)).toUri();
        RestDocs restDocs = new RestDocs(uri);
        return new ResponseEntity<>(resource, restDocs.getHttpHeaders(), HttpStatus.OK);
    }

    @GetMapping(value = "/{idx}/usedskills", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getDoneProjectUsedSkills(@PathVariable Long idx) {

        if(doneProjectService.findByDoneProjectUsedSkills(idx))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Resources<?> resources = doneProjectService.getDoneProjectUsedSkills(idx);
        resources.add(linkTo(methodOn(DoneProjectController.class).getDoneProjectUsedSkills(idx)).withSelfRel());

        URI uri = linkTo(methodOn(DoneProjectController.class).getDoneProjectUsedSkills(idx)).toUri();
        RestDocs restDocs = new RestDocs(uri);
        return new ResponseEntity<>(resources, restDocs.getHttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> postDoneProject(@Valid @RequestBody DoneProjectDTO doneProjectDTO, BindingResult result,
                                             HttpServletRequest request) {

        if (result.hasErrors()) {
            StringBuilder msg = doneProjectService.validation(result);
            return new ResponseEntity<>(msg.toString(), HttpStatus.BAD_REQUEST);
        }

        URI uri = linkTo(methodOn(DoneProjectController.class).postDoneProject(doneProjectDTO, result, request)).toUri();
        RestDocs restDocs = new RestDocs(uri);

        return doneProjectService.postDoneProject(doneProjectDTO, request, restDocs);
    }

    @PutMapping(value = "/{idx}")
    public ResponseEntity<?> putDoneProject(@Valid @RequestBody DoneProjectDTO doneProjectDTO,  BindingResult result,
                                            @PathVariable Long idx, HttpServletRequest request) {
        if (result.hasErrors()) {
            StringBuilder msg = doneProjectService.validation(result);
            return new ResponseEntity<>(msg.toString(), HttpStatus.BAD_REQUEST);
        }

        URI uri = linkTo(methodOn(DoneProjectController.class).putDoneProject(doneProjectDTO, result, idx, request)).toUri();
        RestDocs restDocs = new RestDocs(uri);

        return doneProjectService.putProject(doneProjectDTO, idx, request, restDocs);
    }

    @DeleteMapping(value = "/{idx}")
    public ResponseEntity<?> deleteDoneProject(@PathVariable Long idx, HttpServletRequest request) {
        URI uri = linkTo(methodOn(DoneProjectController.class).deleteDoneProject(idx, request)).toUri();
        RestDocs restDocs = new RestDocs(uri);

        return doneProjectService.deleteDoneProject(idx, request, restDocs);
    }
}
