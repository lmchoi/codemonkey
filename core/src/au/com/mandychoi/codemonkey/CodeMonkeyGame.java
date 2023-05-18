package au.com.mandychoi.codemonkey;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

public class CodeMonkeyGame extends Game {
  private SpriteBatch batch;
  private BitmapFont font;

  @Override
  public void create() {
    batch = new SpriteBatch();
    font = new BitmapFont();
    this.setScreen(new MainMenuScreen(this));
  }

  @Override
  public void render() {
    super.render();
  }

  @Override
  public void dispose() {
    batch.dispose();
    font.dispose();
  }

  public void setupProjection(Matrix4 cameraCombined) {
    batch.setProjectionMatrix(cameraCombined);
  }

  public void beginRendering() {
    batch.begin();
  }

  public void endRendering() {
    batch.end();
  }

  public void drawText(String text, int x, int y) {
    font.draw(batch, text, x, y);
  }

  public void draw(Texture texture, float x, float y) {
    batch.draw(texture, x, y);
  }
}
