package com.github.glusk2.sprouts.core.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.github.glusk2.sprouts.core.MainScreen;

/**
 * A "New game" button's InputListener.
 * <p>
 * A ResetDialog is shown on click, enabling any player to start a new game.
 */
public final class ResetDialog extends InputListener {
    /** The Game to show the new Screen to once the reset is confirmed. */
    private final Game game;
    /** The ShapeRenderer to use in the new Screen after the reset. */
    private final ShapeRenderer renderer;
    /** The stage to "pin" the "reset game" Dialog to. */
    private final Stage stage;
    /** The slider to pick the number of sprouts for the new game with. */
    private final Slider slider;

    /**
     * Creates a "New game" button InputListener.
     *
     * @param game the Game to show the new Screen to once the reset is
     *             confirmed
     * @param renderer the ShapeRenderer to use in the new Screen after the
     *                 reset
     * @param stage the stage to "pin" the "reset game" Dialog to
     * @param slider the slider to pick the number of sprouts for the new game
     *               with
     */
    public ResetDialog(
        final Game game,
        final ShapeRenderer renderer,
        final Stage stage,
        final Slider slider
    ) {
        this.game = game;
        this.renderer = renderer;
        this.stage = stage;
        this.slider = slider;
    }

    /**
     * Creates and shows a ResetDialog to the {@code stage}.
     * <p>
     * {@inheritDoc}
     */
    @Override
    public boolean touchDown(
        final InputEvent event,
        final float x,
        final float y,
        final int pointer,
        final int button
    ) {
        Skin skin = new Skin(Gdx.files.internal("default/skin/uiskin.json"));

        Dialog dialog = new Dialog("Warning!", skin) {
            @Override
            public void result(final Object obj) {
                if (obj instanceof Boolean) {
                    Boolean reset = (Boolean) obj;
                    if (reset) {
                        game.setScreen(
                            new MainScreen(
                                game,
                                stage.getViewport(),
                                renderer,
                                (int) slider.getValue()
                            )
                        );
                    }
                }
            }
        };
        dialog.text(
            new StringBuilder()
                .append("Are you sure you want start a new game?")
                .append("\r\n")
                .append("The current game will be lost.")
                .toString()
        );
        dialog.button("Yes", true);
        dialog.button("No", false);

        dialog.show(stage);
        return true;
    }
}
