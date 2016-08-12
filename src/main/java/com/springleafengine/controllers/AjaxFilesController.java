package com.springleafengine.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.Gson;

/**
 * Handles requests for the application Ajax Files.
 */
@Controller
public class AjaxFilesController {
	
	private static final String IMAGES_PATH = "springleafengine-images/examples";
	private static final Logger logger = LoggerFactory.getLogger(AjaxFilesController.class);
	
	public AjaxFilesController() {
		super();
	}
	
	@RequestMapping({"/url-handler"})
	public @ResponseBody String UploadUrlHandler(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		UploadOptions uplodadOptions = UploadOptions.Builder.withGoogleStorageBucketName(IMAGES_PATH);
		
		String formUrl = blobstoreService.createUploadUrl("/upload", uplodadOptions);
		
		Gson gson = new Gson();
        return gson.toJson(formUrl);
	}
	
	@RequestMapping({"/upload"})
	public @ResponseBody byte[] download(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
	        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	        ImagesService imagesService = ImagesServiceFactory.getImagesService();

	        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
	        List<BlobKey> blobKeys = blobs.get("files[]");
	        
	        // Key store to perform operations on the file
	        String fileBlobKey = blobKeys.get(0).getKeyString();
	        
	        // File URL to display the image directly
	        String fileUrl = imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(blobKeys.get(0)));
	        
	        // We can get the other form fields as usual
	        //String title = req.getParameter("title");
	        //String description = req.getParameter("description");
	        
	        // To create a blobKey object from a String blobKey
	        //BlobKey blobKeyImage = blobstoreService.createGsBlobKey(fileBlobKey);
    
            JsonStructure json = new JsonStructure();
            json.title = "a title for image"; // title;
            json.description = "a description for image"; //description;
            json.url = fileUrl;
            json.blobKey = fileBlobKey;
            json.message = "Images downloaded successfully";
            
            Gson gson = new Gson();
            return gson.toJson(json).getBytes("UTF-8");
	}
	
	public class JsonStructure{
	    public String title;
	    public String description;
	    public String url;
	    public String blobKey;
	    public String message;
	}
	
}