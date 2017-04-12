package com.zslin.tools.docx4j;

import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.ContentAccessor;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;

import javax.xml.bind.JAXBElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/12 10:43.
 */
public class ImageAdd {

    /**
     * Create image, without specifying width
     */
    public static org.docx4j.wml.P newImage( WordprocessingMLPackage wordMLPackage,
                                             byte[] bytes,
                                             String filenameHint, String altText,
                                             int id1, int id2) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        Inline inline = imagePart.createImageInline( filenameHint, altText,
                id1, id2, false);

        // Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.P  p = factory.createP();
        org.docx4j.wml.R  run = factory.createR();
        p.getContent().add(run);
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);

        return p;

    }

    /**
     * Create image, specifying width in twips
     */
    public static org.docx4j.wml.P newImage( WordprocessingMLPackage wordMLPackage,
                                             byte[] bytes,
                                             String filenameHint, String altText,
                                             int id1, int id2, long cx) throws Exception {

        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        Inline inline = imagePart.createImageInline( filenameHint, altText,
                id1, id2, cx, false);

        // Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.P  p = factory.createP();
        org.docx4j.wml.R  run = factory.createR();
        p.getContent().add(run);
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);

        return p;
    }

    public static void replaceImage(WordprocessingMLPackage wordMLPackage,
                                    String replace,
                                    byte[] bytes,
                                    String filenameHint, String altText) throws Exception{
        List texts = getAllElementFromObject(wordMLPackage.getMainDocumentPart(), Text.class);
        for (Object text : texts) {
            Text content = (Text) text;
            if(content.getValue().equals(replace)){
                R r=(R) content.getParent();
                r.getContent().clear();
                P p=(P) r.getParent();
                p.getContent().add(newImage(wordMLPackage, bytes, filenameHint, altText));
            }
        }
    }

    public static org.docx4j.wml.R newImage( WordprocessingMLPackage wordMLPackage,
                                             byte[] bytes,
                                             String filenameHint, String altText) throws Exception {
        int id1=(int) ((Math.random()*1000)*(Math.random()*1000));
        int id2=(int) ((Math.random()*1000)*(Math.random()*1000));
// BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, new File(file));
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordMLPackage, bytes);

        Inline inline = imagePart.createImageInline( filenameHint, altText,
                id1, id2, false);

// Now add the inline in w:p/w:r/w:drawing
        org.docx4j.wml.ObjectFactory factory = Context.getWmlObjectFactory();
        org.docx4j.wml.R run = factory.createR();
        org.docx4j.wml.Drawing drawing = factory.createDrawing();
        run.getContent().add(drawing);
        drawing.getAnchorOrInline().add(inline);
        return run;
    }

    /**
     * 遍历所有的Text，或者Table，或者R或者P等等
     *
     * @param obj
     * @param toSearch
     * @return
     */
    private static List getAllElementFromObject(Object obj, Class<?> toSearch) {
        List result = new ArrayList();
        if (obj instanceof JAXBElement)
            obj = ((JAXBElement<?>) obj).getValue();
        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }
        }
        return result;
    }
}
