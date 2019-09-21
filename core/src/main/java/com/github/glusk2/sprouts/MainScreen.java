package com.github.glusk2.sprouts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The main screen of the application with the toolbar and the game board.
 */
public final class MainScreen extends ScreenAdapter {
    /**
     * This is multiplied by the {@code viewport} height to set the height of
     * the game board area.
     */
    private static final float GAME_BOARD_HEIGHT_FRACTION = .9f;
    /** The spacing between the elements in a toolbar row. */
    private static final int TOOLBAR_ROW_CELL_SPACING = 10;
    /** The padding of the toolbar row. */
    private static final int TOOLBAR_PADDING = 10;
    /** The spacing between the rows of the root layout. */
    private static final float ROOT_ROW_SPACING = 5;
    /** The padding of the root layout. */
    private static final float ROOT_PADDING = 10;

    /** The viewport of {@code this} screen. */
    private final Viewport viewport;
    /**
     * The {@code ShapeRenderer} object used to draw the game board.
     * <p>
     * The {@code renderer} object is not disposed of in
     * {@code this.dispose()}.
     */
    private final ShapeRenderer renderer;

    /** The root object of Actors on {@code this} screen. */
    private Stage stage;

    /**
     * Constructs a new {@code MainScreen} of the Game by specifying the
     * {@code viewport} and the {@code renderer}.
     * <p>
     * The {@code renderer} object is not disposed of in
     * {@code this.dispose()}.
     *
     * @param viewport the viewport of {@code this} screen
     * @param renderer the {@code ShapeRenderer} object used to draw the game
     *                 board
     */
    public MainScreen(
        final Viewport viewport,
        final ShapeRenderer renderer
    ) {
        this.viewport = viewport;
        this.renderer = renderer;
    }

    /**
     * Sets up the layout of the UI components (i.e., <em>stages</em> the
     * <em>actors</em>).
     * <p>
     * {@inheritDoc}
     */
    @Override
    public void show() {
        stage = new Stage(viewport);

        // ToDo: Add input listener to the resetButton actor
        TextButton resetButton =
            new TextButton(
                "Reset",
                new Skin(
                    Gdx.files.internal("default/skin/uiskin.json")
                )
            );
        // ToDo: Add input listener to the gameBoard actor
        Actor gameBoard = new GameBoardSheet(renderer);
        gameBoard.setSize(
            stage.getViewport().getWorldWidth() - 2 * ROOT_PADDING,
            stage.getViewport().getWorldHeight() * GAME_BOARD_HEIGHT_FRACTION
        );

        Table toolbar = new Table().pad(TOOLBAR_PADDING);
        toolbar.left().add(resetButton).space(TOOLBAR_ROW_CELL_SPACING);

        VerticalGroup rootLayout =
            new VerticalGroup()
                .center()
                .space(ROOT_ROW_SPACING)
                .pad(ROOT_PADDING);
        rootLayout.setFillParent(true);
        rootLayout.grow().addActor(toolbar);
        rootLayout.addActor(gameBoard);

        stage.addActor(rootLayout);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
