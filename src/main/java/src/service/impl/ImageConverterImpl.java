package src.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import src.enums.ErrorCode;
import src.model.Image;
import src.model.exceptions.GenericReaderException;
import src.service.ImageConverter;

@Service
public class ImageConverterImpl implements ImageConverter {

    private static Logger logger = LoggerFactory.getLogger(ImageConverterImpl.class);

    public Image convert(String imageText) {

        validateImageText(imageText);
        
        String[] splitImage = imageText.split("\\n");
        int imageWidth = getImageWidth(splitImage);
        String format = getFormat(imageWidth);

        char [][] graph = new char[splitImage.length][];

        for (int index = 0; index < splitImage.length; index++) {
            graph[index] = String.format(format, splitImage[index], ' ').toCharArray();
        }
        
        Image image = new Image();
        image.setGraph(graph);
        image.setWidth(imageWidth);
        image.setHeight(splitImage.length);

        logger.info("Successfully converted requested image body. width={} height={}", image.getWidth(), image.getHeight());

        return image;
    }
    
    private void validateImageText(String imageText) {
        if (StringUtils.isEmpty(imageText)) {
            throw GenericReaderException.builder()
                    .errorCode(ErrorCode.EMPTY_IMAGE_TEXT)
                    .message("Cannot convert an empty image")
                    .build();
        }
    }

    private String getFormat(int length) {
        return "%-" + length + "s";
    }

    private int getImageWidth(String[] splitImage) {
        int maxWdith = 0;

        for (String line: splitImage) {
            if (line.length() > maxWdith)
                maxWdith = line.length();
        }

        return maxWdith;
    }
}
