package com.github.glusk2.sprouts.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.glusk2.sprouts.MainScreen;

/**
 * A "reset" button's InputListener.
 * <p>
 * A "reset game" Dialog is shown on click, enabling any player to reset the
 * current game.
 */
public final class ResetDialog extends InputListener {
    /** The Game to show the new Screen to once the reset is confirmed. */
    private final Game game;
    /** The ShapeRenderer to use in the new Screen after the reset. */
    private final ShapeRenderer renderer;
    /** The stage to "pin" the "reset game" Dialog to. */
    private final Stage stage;

    /**
     * Creates a new "reset" button InputListener.
     *
     * @param game the Game to show the new Screen to once the reset is
     *             confirmed
     * @param renderer the ShapeRenderer to use in the new Screen after the
     *                 reset
     * @param stage the stage to "pin" the "reset game" Dialog to
     */
    public ResetDialog(
        final Game game,
        final ShapeRenderer renderer,
        final Stage stage
    ) {
        this.game = game;
        this.renderer = renderer;
        this.stage = stage;
    }

    /**
     * Creates and shows a new "reset game" Dialog to the {@code stage}.
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
                                renderer
                            )
                        );
                    }
                }
            }
        };
        dialog.text("Are you sure you want to reset the game?");
        dialog.button("Yes", true);
        dialog.button("No", false);

        dialog.show(stage);
        return true;
    }
}
