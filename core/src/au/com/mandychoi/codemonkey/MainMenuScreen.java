package au.com.mandychoi.codemonkey;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class MainMenuScreen implements Screen {

    final CodeMonkeyGame game;

    private OrthographicCamera camera;

    // game assets
    private Texture dropImage;
    private Texture bucketImage;
    private Sound dropSound;

    // game objects
    private Rectangle bucket;
    private Vector3 touchPos = new Vector3();
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    private int dropsGathered;

    public MainMenuScreen(CodeMonkeyGame game) {
        this.game = game;

        dropImage = new Texture(Gdx.files.internal("droplet.png"));
        bucketImage = new Texture(Gdx.files.internal("bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound = Gdx.audio.newSound(Gdx.files.internal("droplet.wav"));

        // rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));

        // // start the playback of the background music immediately
        // rainMusic.setLooping(true);
        // rainMusic.play();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // create game objects
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;

        raindrops = new Array<Rectangle>();
        spawnRaindrop();
    }

    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800 - 64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
		// clear the screen
		ScreenUtils.clear(0, 0, 0.2f, 1);

		// setup the camera
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		
        game.batch.begin();
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for(Rectangle raindrop: raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		 }
		game.batch.end();

		// controls
		if (Gdx.input.isTouched()) {
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bucket.x = touchPos.x - 64 / 2;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			bucket.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			bucket.x += 200 * Gdx.graphics.getDeltaTime();

		// boundaries
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > 800 - 64)
			bucket.x = 800 - 64;

		// do game stuff
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

        Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
			if (raindrop.y + 64 < 0)
				iter.remove();

			if(raindrop.overlaps(bucket)) {
                dropsGathered++;
				dropSound.play();
				iter.remove();
			}
		}
    }

    @Override
    public void show() {
        // play music
    }

    @Override
    public void resize(int width, int height) {
        // do nothing for now
    }

	@Override
	public void pause () {
		// show pause screen
	}

	@Override
	public void resume () {
		// clear pause screen
	}

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
    }
}
