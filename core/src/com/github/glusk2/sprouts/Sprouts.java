package com.github.glusk2.sprouts;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Sprouts extends InputAdapter implements ApplicationListener {

    private Viewport viewport;

    /** Most recent, unprojected touch/mouse position. */
    private Vector3 currentPosition;
    private ShapeRenderer shapes;

    public Sprouts() {
        this(1280, 720);
    }

    public Sprouts(int minWidth, int minHeight) {
        this(
            new ExtendViewport(
                minWidth / 32f,
                minHeight / 32f,
                new OrthographicCamera()
            )
        );
    }

    public Sprouts(Viewport viewport) {
        this.viewport = viewport;
    }


    @Override
    public void create() {
        shapes = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }
    @Override
    public void resize(int width, int height) {
        currentPosition = null;

        viewport.update(width, height, true);

        Gdx.gl.glClearColor(1, 1, 1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render() {
        if (currentPosition != null) {
            shapes.setProjectionMatrix(viewport.getCamera().combined);
            shapes.setColor(Color.BLACK);
            shapes.begin(ShapeRenderer.ShapeType.Filled);
            shapes.circle(currentPosition.x, currentPosition.y, 0.125f, 16);
            shapes.end();
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        shapes.dispose();
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        currentPosition =
            viewport.getCamera().unproject(new Vector3(screenX, screenY, 0));
        return true;
    }
}
