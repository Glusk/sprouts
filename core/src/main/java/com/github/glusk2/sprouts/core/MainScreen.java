package com.github.glusk2.sprouts.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.glusk2.sprouts.core.snapshots.BeforeMove;
import com.github.glusk2.sprouts.core.snapshots.GameBoard;
import com.github.glusk2.sprouts.core.snapshots.ResetDialog;
import com.github.glusk2.sprouts.core.snapshots.TouchEventSnapshooter;

/**
 * The main screen of the application with the toolbar and the game board.
 */
public final class MainScreen extends ScreenAdapter {
    /** Starting sprouts slider minimum value. */
    private static final int SLIDER_MIN = 2;
    /** Starting sprouts slider maximum value. */
    private static final int SLIDER_MAX = 7;
    /** Starting sprouts slider step value. */
    private static final int SLIDER_STEP = 1;

    /** The spacing between the elements in a toolbar. */
    private static final int TOOLBAR_CELL_SPACING = 10;
    /** The padding of the toolbar row. */
    private static final int TOOLBAR_PADDING = 10;
    /** The spacing between the rows of the root layout. */
    private static final float ROOT_ROW_SPACING = 5;
    /** The padding of the root layout. */
    private static final float ROOT_PADDING = 10;

    /**
     * Circle segment count.
     * <p>
     * Defines the number of points that define the border polyline of circles
     * drawn on screen.
     */
    private static final int CIRCLE_SEGMENT_COUNT = 16;


    /** The Game instance that {@code this} Screen belongs to. */
    private final Game game;

    /** The viewport of {@code this} screen. */
    private final Viewport viewport;
    /**
     * Minimum dimension ratio.
     * <p>
     * This is used to compute the move thickness. The minimum game board
     * dimension is divided by this value to produce the move thickness.
     */
    private final float minDimensionRatio;
    /**
     * The {@code ShapeRenderer} object used to draw the game board.
     * <p>
     * The {@code renderer} object is not disposed of in
     * {@code this.dispose()}.
     */
    private final ShapeRenderer renderer;

    /** The root object of Actors on {@code this} screen. */
    private Stage stage;

    /** The number of starting sprouts to generate. */
    private int numOfSprouts;

    /**
     * A switch that tracks whether the user wants to display the cobweb.
     * Initially, the cobweb is disabled.
     */
    private CobwebSwitch displayCobweb = new CobwebSwitch(false);

    /**
     * Constructs a new {@code MainScreen} of the Game by specifying the
     * {@code game}, {@code viewport}, {@code renderer} and the
     * {@code numOfSprouts}.
     * <p>
     * The {@code renderer} object is not disposed of in
     * {@code this.dispose()}.
     *
     * @param game the Game instance that {@code this} Screen belongs to
     * @param viewport the viewport of {@code this} screen
     * @param minDimensionRatio Minimum dimension ratio. This is used to
     *        compute the move thickness. The minimum game board dimension
     *        is divided by this value to produce the move thickness.
     * @param renderer the {@code ShapeRenderer} object used to draw the game
     *                 board
     * @param numOfSprouts the number of starting sprouts to generate
     */
    public MainScreen(
        final Game game,
        final Viewport viewport,
        final float minDimensionRatio,
        final ShapeRenderer renderer,
        final int numOfSprouts
    ) {
        this.game = game;
        this.viewport = viewport;
        this.minDimensionRatio = minDimensionRatio;
        this.renderer = renderer;
        this.numOfSprouts = numOfSprouts;
    }

    /**
     * Sets up the layout of the UI components (i.e., <em>stages</em> the
     * <em>actors</em>).
     * <p>
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("checkstyle:methodlength")
    public void show() {
        stage = new Stage(viewport);

        Skin skin =
            new Skin(
                Gdx.files.internal("default/skin/uiskin.json")
            );

        final Label sliderLabel = new Label("" + numOfSprouts, skin);
        sliderLabel.setColor(Color.BLACK);

        final Slider slider =
            new Slider(
                SLIDER_MIN,
                SLIDER_MAX,
                SLIDER_STEP,
                false,
                skin
            );
        slider.addListener(new EventListener() {
            @Override
            public boolean handle(final Event event) {
                sliderLabel.setText("" + (int) slider.getValue());
                return true;
            }
        });
        slider.setValue(numOfSprouts);

        TextButton resetButton =
            new TextButton(
                "New game",
                skin
            );
        resetButton.addListener(
            new ResetDialog(game, renderer, minDimensionRatio, stage, slider)
        );

        TextButton helpButton =
            new TextButton(
                "How to play?",
                skin
            );
        helpButton.addListener(
            new InputListener() {
                @Override
                public boolean touchDown(
                    final InputEvent event,
                    final float x,
                    final float y,
                    final int pointer,
                    final int button
                ) {
                    Window instrWindow = new Window("Instructions", skin);

                    Label instructions = new Label(
                        "Connect two living sprouts with a curve that\r\n"
                      + "doesn't cross itself or the existing moves.\r\n"
                      + "\r\n"
                      + "A move can intersect red lines.\r\n"
                      + "\r\n"
                      + "Once a move is complete, add a new sprout anywhere\r\n"
                      + "along the curve.",
                        skin
                    );
                    instructions.setWrap(false);

                    TextButton okButton = new TextButton("Okay!", skin);
                    okButton.addListener(new InputListener() {
                        @Override
                        public boolean touchDown(
                            final InputEvent event,
                            final float x,
                            final float y,
                            final int pointer,
                            final int button
                        ) {
                            instrWindow.setVisible(false);
                            return true;
                        }
                    });

                    instrWindow.add(instructions);
                    instrWindow.row();
                    instrWindow.add(okButton);
                    instrWindow.pack();

                    stage.addActor(instrWindow);
                    return true;
                }
            }
        );

        TextButton cobwebToggle =
            new TextButton(
                "Display cobweb",
                skin
            );
        cobwebToggle.addListener(
            new InputListener() {
                @Override
                public boolean touchDown(
                    final InputEvent event,
                    final float x,
                    final float y,
                    final int pointer,
                    final int button
                ) {
                    String buttonText = cobwebToggle.getText().toString();
                    if (buttonText.equals("Display cobweb")) {
                        cobwebToggle.setText("Hide cobweb");
                        cobwebToggle.setColor(Color.RED);
                    } else {
                        cobwebToggle.setText("Display cobweb");
                        cobwebToggle.setColor(Color.LIGHT_GRAY);
                    }
                    displayCobweb.toggle();
                    return true;
                }
            }
        );

        Table toolbar = new Table().pad(TOOLBAR_PADDING);
        toolbar.add(helpButton);
        toolbar.left().add(resetButton).space(TOOLBAR_CELL_SPACING);
        toolbar.add(sliderLabel).space(TOOLBAR_CELL_SPACING);
        toolbar.add(slider).space(TOOLBAR_CELL_SPACING);
        toolbar.add(cobwebToggle);
        toolbar.setHeight(resetButton.getPrefHeight() + 2 * TOOLBAR_PADDING);


        Rectangle gameBounds =
            new Rectangle(
                ROOT_PADDING,
                ROOT_PADDING,
                stage.getViewport().getWorldWidth() - 2 * ROOT_PADDING,
                stage.getViewport().getWorldHeight()
                - 2 * ROOT_PADDING
                - toolbar.getHeight()
                - ROOT_ROW_SPACING
            );
        TouchEventSnapshooter gameBoardListener =
            new TouchEventSnapshooter(
                new BeforeMove(
                    Math.min(
                        gameBounds.getWidth(),
                        gameBounds.getHeight()
                    ) / minDimensionRatio,
                    CIRCLE_SEGMENT_COUNT,
                    (int) slider.getValue(),
                    gameBounds,
                    displayCobweb
                )
            );
        Actor gameBoard = new GameBoard(gameBoardListener, renderer);
        gameBoard.setBounds(
            gameBounds.getX(),
            gameBounds.getY(),
            gameBounds.getWidth(),
            gameBounds.getHeight()
        );

        gameBoard.addListener(gameBoardListener);

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
