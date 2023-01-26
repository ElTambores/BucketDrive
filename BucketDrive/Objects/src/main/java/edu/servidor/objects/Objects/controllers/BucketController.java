package edu.servidor.objects.Objects.controllers;

import edu.servidor.objects.Objects.forms.BucketForm;
import edu.servidor.objects.Objects.models.Bucket;
import edu.servidor.objects.Objects.models.ObjectFile;
import edu.servidor.objects.Objects.models.User;
import edu.servidor.objects.Objects.services.BucketService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class BucketController {
    @Autowired
    BucketService bucketService;

    @GetMapping("/objects")
    public String objects(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        model.addAttribute("buckets", bucketService.getBuckets(user));
        return "objects";
    }

    @PostMapping("/objects")
    public String objects(@Valid BucketForm bucketForm, BindingResult bindingResult, Model model, HttpSession session) {
        String message = "";
        User user = (User) session.getAttribute("currentUser");
        if (bindingResult.hasErrors()) {
            message = "Data input error";
        } else {
            message = bucketService.createBucket(user, bucketForm.getUri());
        }
        model.addAttribute("message", message);
        model.addAttribute("buckets", bucketService.getBuckets(user));
        return "objects";
    }

    @GetMapping("/objects/{name}/")
    public String showBucket(@PathVariable String name, HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        int bucketID = bucketService.getBucketID(name, user.getUsername());
        List<ObjectFile> objects = bucketService.getObjectsFromBucket(bucketID);
        model.addAttribute("bucket", name);
        model.addAttribute("objects", objects);
        return "bucket";
    }

    @PostMapping("/objects/{name}/")
    public String createObject(@PathVariable String name, @RequestParam("file") MultipartFile file, @RequestParam("path") String path, Model model, HttpSession session) throws IOException {
        User user = (User) session.getAttribute("currentUser");
        Bucket bucket = bucketService.getBucketByNameOwner(name, user.getUsername());
        String message = bucketService.createObject(file, path, bucket, user);
        List<ObjectFile> objects = bucketService.getObjectsFromBucket(bucket.getId());

        model.addAttribute("message", message);
        model.addAttribute("bucket", bucket);
        model.addAttribute("objects", objects);

        return ("redirect:/objects/" + bucket.getUri() + "/");
    }

    @PostMapping("/deletebucket/{id}")
    public String deleteBucket(@PathVariable int id, Model model, HttpSession session) {
        model.addAttribute("message", bucketService.deleteBucket(id));
        model.addAttribute("buckets", bucketService.getBuckets((User) session.getAttribute("currentUser")));
        return "objects";
    }

//    @GetMapping("/download/{objid}/{fid}")
//    public ResponseEntity<byte[]> download(@PathVariable int objid, @PathVariable int fid) {
//        ObjectFile objectFile = new ObjectFile(); //TODO obtener el objeto Y versión solicitadas por le usuario
//        byte[] fileContent = objectFile.getBody();
//        String name = objectFile.getUri(); //TODO hacer service que extraiga el nombre del archivo, no hay que devolver bucket/carpeta/archivo.txt hay que devolver archivo.txt
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.valueOf(objectFile.getContentType()));
//        headers.setContentLength(objectFile.getContentLength());
//        headers.set("Content-disposition", "attachment;filename=" + name);
//        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
//    }
}