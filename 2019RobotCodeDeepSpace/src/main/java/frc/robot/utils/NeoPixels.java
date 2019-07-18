package frc.robot.utils;

import org.usfirst.frc.team4999.lights.AnimationCoordinator;
import org.usfirst.frc.team4999.lights.Animator;
import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.animations.*;

public class NeoPixels {

  private static final int PRESET_MODE_OVERLAY_START = 20;
  private static final int PRESET_MODE_OVERLAY_LENGTH = 40;

  private Animator animator;
  private AnimationCoordinator coordinator;

  /* BEGIN BASE ANIMATION OPTIONS */

  private static Color[] rainbowcolors = { new Color(139, 0, 255), Color.BLUE, Color.GREEN, Color.YELLOW,
      new Color(255, 127, 0), Color.RED };

  public final Animation rainbow = new AnimationSequence(new Animation[] { Snake.rainbowSnake(70),
      Fade.rainbowFade(100, 20), new Bounce(Color.WHITE, rainbowcolors, 20, 50), new Stack(rainbowcolors, 25, 40),
      new BounceStack(rainbowcolors, 7, 40) }, new int[] { 5000, 5000, 10000, 10000, 10000 });

  public final Animation momentum = new AnimationSequence(
      new Animation[] { Snake.twoColorSnake(Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, 1, 5, 2, 40),
          new Fade(new Color[] { Color.MOMENTUM_BLUE, Color.WHITE, Color.MOMENTUM_PURPLE }, 200, 0),
          new Bounce(Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, 8, 20, 50),
          new BounceStack(
              new Color[] { Color.MOMENTUM_PURPLE, Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, Color.MOMENTUM_BLUE }, 8,
              40), },
      new int[] { 5000, 5000, 10000, 5000 });

  /* END BASE ANIMATION OPTIONS */

  private final Animation hatch_preset_mode_overlay = new ClippedAnimation(new Solid(Color.GREEN),
      PRESET_MODE_OVERLAY_START, PRESET_MODE_OVERLAY_LENGTH);
  private final Animation cargo_preset_mode_overlay = new ClippedAnimation(new Solid(Color.YELLOW),
      PRESET_MODE_OVERLAY_START, PRESET_MODE_OVERLAY_LENGTH);

  public NeoPixels() {
    // Put this in a try-catch because a LED error shouldn't crash the whole robot
    try {
      animator = new Animator();
      coordinator = new AnimationCoordinator(animator);
    } catch (Exception e) {
      e.printStackTrace();
      if (animator != null) {
        animator.stopAnimation();
        animator = null;
      }
    }
  }

  public void setBaseAnimation(Animation animation) {
    if (coordinator == null)
      return;
    coordinator.setBase(animation);
  }

  public void selectHatchPresetMode() {
    if (coordinator == null)
      return;
    if (coordinator.hasAnimation("cargo_preset_mode"))
      coordinator.popAnimation("cargo_preset_mode");
    if (!coordinator.hasAnimation("hatch_preset_mode"))
      coordinator.pushAnimation("hatch_preset_mode", hatch_preset_mode_overlay, true);
  }

  public void selectCargoPresetMode() {
    if (coordinator == null)
      return;
    if (coordinator.hasAnimation("hatch_preset_mode"))
      coordinator.popAnimation("hatch_preset_mode");
    if (!coordinator.hasAnimation("cargo_preset_mode"))
      coordinator.pushAnimation("cargo_preset_mode", cargo_preset_mode_overlay, true);
  }

}