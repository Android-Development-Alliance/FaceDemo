package com.yudan.face;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (: Author：llk
 * (: WorkSpace: PhoneLink
 * (: CreateDate: 2016/12/13
 * (: Describe:
 */

public class FaceMaster {

    private static FaceMaster faceMaster = null;
    private static Context mContext = null;

    /**
     * wechat face-----------------------------------------------------
     */
    private static final String ASSETS_FACE_FILE = "face";
    private static final String ASSETS_FACE_RESOURCE_FLODER = "face_icon";

    //face tab list
    List<String> faceTabList = new ArrayList<>();
    //show face position
    List<Integer> facePositionList = new ArrayList<>();

    //face tag about three type
    //example:
    //哭 ->save in oneStringList
    //微笑 ->save in twoStringList
    //左哼哼 ->save in threeStringList
    private List<FaceInfo> oneStringList = new ArrayList<>();
    private List<FaceInfo> twoStringList = new ArrayList<>();
    private List<FaceInfo> threeStringList = new ArrayList<>();

    /**
     * emoji-----------------------------------------------------
     */
    private List<String> emojiTabList = new ArrayList<>();

    ////android 6.0 emoji
    public static final String COLOR_EMOJI_FONT = "ColorEmojiFont.ttf";
    //android 4.0 emoji
    public static final String BLACK_EMOJI_FONT = "AndroidEmoji.ttf";

    public static FaceMaster getInstace(Context context){
        if(faceMaster == null){ faceMaster = new FaceMaster(); }

        if (context != null){ mContext = context; }

        return faceMaster;
    }

    /**
     * support must be set the Typeface in view
     * @return Typeface
     */
    public Typeface getTypeface(){
        if(mContext == null) return Typeface.DEFAULT;

        Typeface typeface = Typeface
                .createFromAsset(mContext.getResources().getAssets(), COLOR_EMOJI_FONT);

        return typeface;
    }

    public Typeface getTypeface(String path){
        if(mContext == null) return Typeface.DEFAULT;

        Typeface typeface = Typeface
                .createFromAsset(mContext.getResources().getAssets(), path);

        return typeface;
    }

    /**
     * match face to message
     *
     * @param message
     * @return SpannableString
     */
    public SpannableString matchFaceToMessage(String message){
        if(mContext == null) return SpannableString.valueOf(message);

        //首先判断emoji (因为emoji只是替换内容，无需用到SpannableString)
        if(checkEmojiInText(message)){
            for(int i = 0; i < emojiTabList.size(); i++){
                String emojiTab = emojiTabList.get(i).replace("<emoji", "")
                        .replace(">", "")
                        .toUpperCase();
                //string to 16进制 int
                int code = Integer.valueOf(emojiTab, 16);
                //转换成emoji
                String emoji = String.valueOf(Character.toChars(code));
                message = message.replace(emojiTabList.get(i), emoji);
            }
            LogUtils.i("new message is " + message);

            emojiTabList.clear();
        }

        //接着判断 face
        SpannableString spannableString = new SpannableString(message);
        if(checkFaceInText(message)){
            //prepare chat face list
            prepareChatFace();

            for (int i = 0; i < faceTabList.size(); i++) {
                Bitmap res = findRelevantFace(faceTabList.get(i)
                        .replace("[", "")
                        .replace("]", ""));
                if (res == null) continue;

                ImageSpan imageSpan = new ImageSpan(mContext, res);
                int startPos = facePositionList.get(i);
                int endPos = startPos + faceTabList.get(i).length();
                LogUtils.e("start " + startPos + " end " + endPos);

                spannableString.setSpan(imageSpan, startPos, endPos,
                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }

            faceTabList.clear();
            facePositionList.clear();
        }

        return spannableString;
    }

    private boolean checkEmojiInText(String text){
        if(text == null && text.equals(""))return false;

        //正则：取<emoji 和 >之间的内容(包含<emoji 和 >)
        Pattern p = Pattern.compile("<emoji(.*?)>");
        Matcher matcher = p.matcher(text);
        while (matcher.find()) {
            emojiTabList.add(matcher.group());
        }

        if(emojiTabList.size() > 0){
            return true;
        }

        return false;
    }

    /**
     * Check up the message whether have face
     *
     * @param text
     * @return true: have face
     */
    private boolean checkFaceInText(String text){
        if(text == null && text.equals(""))return false;

        //正则：以[开头, 以]结尾,中间至少出现一个字符，但是不能超过三个
        Pattern p = Pattern.compile("\\[.{1,3}?\\]");
        Matcher matcher = p.matcher(text);
        while (matcher.find()) {
            faceTabList.add(matcher.group());
            facePositionList.add(matcher.start());
        }

        if(faceTabList.size() > 0
                && facePositionList.size() > 0
                && faceTabList.size() == facePositionList.size()){
            return true;
        }

        return false;
    }

    /**
     * prepare all chat face to assign list
     */
    public void prepareChatFace(){
        if(mContext == null)return;

        if(oneStringList.size() == 0 && twoStringList.size() == 0 && threeStringList.size() == 0){
            try {
                InputStream inputStream = mContext.getResources().getAssets().open(ASSETS_FACE_FILE);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String lineInfo = null;
                while ((lineInfo = bufferedReader.readLine()) != null) {
                    String[] infoContent = lineInfo.split(",");

                    FaceInfo faceInfo = new FaceInfo();
                    faceInfo.setFaceName(infoContent[1]);
                    faceInfo.setFaceResource(getImageFromAssetsFile(infoContent[0]));

                    //face tag lenght
                    int strLenght = infoContent[1].length();
                    switch (strLenght){
                        case 1:
                            oneStringList.add(faceInfo);
                            break;
                        case 2:
                            twoStringList.add(faceInfo);
                            break;
                        case 3:
                            threeStringList.add(faceInfo);
                            break;
                        default:
                            break;
                    }

                }
            } catch (IOException e) {
                LogUtils.e("get assets file exception. error: " + e.getMessage());
                e.printStackTrace();
            }

            LogUtils.i("prepare all chat face to assign list finish!");
        }
    }

    /**
     * find relevant face
     *
     * @param text
     * @return face resource
     */
    private Bitmap findRelevantFace(String text){
        if(text == null && text.equals(""))return null;
        switch (text.length()){
            case 1:
                for(FaceInfo faceInfo : oneStringList){
                    if(faceInfo.getFaceName().equals(text)){
                        return faceInfo.getFaceResource();
                    }
                }
                break;
            case 2:
                for(FaceInfo faceInfo : twoStringList){
                    if(faceInfo.getFaceName().equals(text)){
                        return faceInfo.getFaceResource();
                    }
                }
                break;
            case 3:
                for(FaceInfo faceInfo : threeStringList){
                    if(faceInfo.getFaceName().equals(text)){
                        return faceInfo.getFaceResource();
                    }
                }
                break;
        }

        return null;
    }

    /**
     * use filename read image in assets
     * @param fileName
     * @return
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        if(mContext == null)return null;

        Bitmap image = null;
        AssetManager am = mContext.getResources().getAssets();
        try
        {
            InputStream is = am.open(ASSETS_FACE_RESOURCE_FLODER+ "/" + fileName + ".png");
            image = BitmapFactory.decodeStream(is);
            is.close();

            if(image != null){
                return image;
            }
        }
        catch (IOException e) {
            LogUtils.e("read face image exception, error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}
