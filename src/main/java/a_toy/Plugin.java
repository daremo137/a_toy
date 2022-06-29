package a_toy;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public final class Plugin extends JavaPlugin {
    public static final Plugin INSTANCE = new Plugin();

    private Plugin() {
        super(new JvmPluginDescriptionBuilder("a_toy.Plugin", "1.0")
//                //插件名
//                .name("shenbi")
//                // 作者
//                .author("Dark")
//                // 描述
//                .info("An toy")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");

        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            String content = event.getMessage().contentToString();
            if(content.startsWith("$") && content.endsWith("$")) {
                try{
                    content = content.length() >= 3? content.substring(1, content.length() - 1) : "";
                    URL url1 = new URL("https://latex.codecogs.com/png.image?\\dpi{110}" + URLEncoder.encode(content, "UTF-8").replace("+", "%20"));
                    URLConnection uc = url1.openConnection();
                    InputStream inputStream = uc.getInputStream();

                    String latex = "/root/latex.png";
                    FileOutputStream out = new FileOutputStream(latex);
                    int j = 0;
                    while ((j = inputStream.read()) != -1) {
                        out.write(j);
                    }
                    inputStream.close();
                    Image image = event.getSubject().uploadImage(ExternalResource.create(new File(latex)));
                    event.getSubject().sendMessage(image);
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}