package utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;

public class S3FileUpload {

    private static final AWSCredentials credentials = new BasicAWSCredentials(
                      "AKIAJAQEEIK5QV64KKYQ", 
                      "TRIGl/nmsxFtXEyzKQvflkcgzco800tNARp62BTV");
    private static final AmazonS3 s3client = new AmazonS3Client(credentials);
    private static final String bucketName = "scrapbookartcom";
    
    public static void uploadFileToS3(File file, String directory, String subDir, String fileName) {

      String s3FileName = directory + "/" + subDir + "/" + fileName;

        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            s3client.putObject(new PutObjectRequest(bucketName, s3FileName, file));

            } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                        "means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                        "means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }
    }

    public static void deleteFileFromS3(String directory, String subDir, String fileName) {
        String s3FileName = directory + "/" + subDir + "/" + fileName;
        try {
            s3client.deleteObject(bucketName, s3FileName);

            } catch (AmazonServiceException ase) {
                System.out.println("Caught an AmazonServiceException, which " +
                        "means your request made it " +
                        "to Amazon S3, but was rejected with an error response" +
                        " for some reason.");
                System.out.println("Error Message:    " + ase.getMessage());
                System.out.println("HTTP Status Code: " + ase.getStatusCode());
                System.out.println("AWS Error Code:   " + ase.getErrorCode());
                System.out.println("Error Type:       " + ase.getErrorType());
                System.out.println("Request ID:       " + ase.getRequestId());
            } catch (AmazonClientException ace) {
                System.out.println("Caught an AmazonClientException, which " +
                        "means the client encountered " +
                        "an internal error while trying to " +
                        "communicate with S3, " +
                        "such as not being able to access the network.");
                System.out.println("Error Message: " + ace.getMessage());
            }
        
    }
    public static String getUrl(String directory, String subDir, String fileName) {
        String url = "https://s3.eu-west-2.amazonaws.com/" + bucketName + "/"
            + directory + "/" + subDir + "/" + fileName;
        return url;
    }
}