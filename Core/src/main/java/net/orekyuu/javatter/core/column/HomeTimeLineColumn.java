package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.column.ColumnController;
import net.orekyuu.javatter.api.column.ColumnState;
import net.orekyuu.javatter.api.service.TwitterUserService;
import net.orekyuu.javatter.api.twitter.TwitterUser;
import net.orekyuu.javatter.api.twitter.model.Tweet;
import net.orekyuu.javatter.api.twitter.userstream.events.OnStatus;

import javax.inject.Inject;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class HomeTimeLineColumn implements ColumnController, Initializable {
    public static final String ID = "timeline";

    private static final String KEY = "userId";
    @FXML
    private ListView<String> timeline;
    @FXML
    private Label title;

    private Optional<TwitterUser> user;
    private static final Logger logger = Logger.getLogger(HomeTimeLineColumn.class.getName());

    @Inject
    private TwitterUserService userService;
    private OnStatus onStatus;

    @Override
    public void restoration(ColumnState columnState) {
        if (columnState == null) {
            columnState = newColumnState();
        }
        logger.info(columnState.toString());
        String userName = (String) columnState.getData(KEY).getFirst();
        user = userService.findTwitterUser(userName);


        Runnable runnable = () -> user
                .map(u -> u.getUser().getScreenName())
                .map(name -> "@" + name)
                .ifPresent(title::setText);
        Platform.runLater(runnable);
        //弱参照でリスナが保持されるためフィールドに束縛しておく
        onStatus = this::onStatus;
        user.ifPresent(user -> user.userStream().onStatus(onStatus));
    }

    private void onStatus(Tweet tweet) {
        Platform.runLater(() -> {
            logger.info("onStatus: " + tweet.getText());
            timeline.getItems().add(tweet.getText());
        });
    }

    @Override
    public String getPluginId() {
        return ColumnInfos.PLUGIN_ID_BUILDIN;
    }

    @Override
    public String getColumnId() {
        return HomeTimeLineColumn.ID;
    }

    @Override
    public void onClose(ColumnState columnState) {
        logger.info("save");
        user.map(TwitterUser::getUser).ifPresent(u -> columnState.setData(KEY, u.getScreenName()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("initialize");
    }
}
