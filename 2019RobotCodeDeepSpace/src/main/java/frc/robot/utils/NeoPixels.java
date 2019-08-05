package frc.robot.utils;

import org.usfirst.frc.team4999.lights.AnimationCoordinator;
import org.usfirst.frc.team4999.lights.Animator;
import org.usfirst.frc.team4999.lights.BrightnessFilter;
import org.usfirst.frc.team4999.lights.Color;
import org.usfirst.frc.team4999.lights.animations.*;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.robot.RobotMap;

public class NeoPixels {

  private static final int PRESET_MODE_OVERLAY_1_START = 73;
  private static final int PRESET_MODE_OVERLAY_1_LENGTH = 100 - PRESET_MODE_OVERLAY_1_START;
  private static final int PRESET_MODE_OVERLAY_2_START = 173;
  private static final int PRESET_MODE_OVERLAY_2_LENGTH = 240 - PRESET_MODE_OVERLAY_2_START;

  private Animator animator;
  private AnimationCoordinator coordinator;

  private NetworkTableEntry brightnessWidget;

  /* BEGIN BASE ANIMATION OPTIONS */

  private static Color[] rainbowcolors = { new Color(139, 0, 255), Color.BLUE, Color.GREEN, Color.YELLOW,
      new Color(255, 127, 0), Color.RED };

  public final Animation rainbow = new AnimationSequence(new Animation[] { Snake.rainbowSnake(70),
      Fade.rainbowFade(100, 20), new Bounce(Color.WHITE, rainbowcolors, 40, 50), new Stack(rainbowcolors, 50, 40),
      new BounceStack(rainbowcolors, 20, 40) }, new int[] { 5000, 5000, 10000, 10000, 10000 });

  public final Animation momentum = new AnimationSequence(
      new Animation[] { Snake.twoColorSnake(Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, 1, 5, 2, 40),
          new Fade(new Color[] { Color.MOMENTUM_BLUE, Color.WHITE, Color.MOMENTUM_PURPLE }, 200, 0),
          new Bounce(Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, 8, 20, 50),
          new BounceStack(
              new Color[] { Color.MOMENTUM_PURPLE, Color.MOMENTUM_PURPLE, Color.MOMENTUM_BLUE, Color.MOMENTUM_BLUE }, 8,
              40), },
      new int[] { 5000, 5000, 10000, 5000 });

  public final Animation socketListener = new SocketListener();

  public final Animation ledIndexer;

  public final Animation blinkRed = new Blink(new Color[] { Color.RED, Color.BLACK }, 50);

  /* END BASE ANIMATION OPTIONS */

  private final Animation hatch_preset_mode_overlay = new Overlay(new Animation[] {
      new ClippedAnimation(new Solid(Color.YELLOW), PRESET_MODE_OVERLAY_1_START, PRESET_MODE_OVERLAY_1_LENGTH),
      new ClippedAnimation(new Solid(Color.YELLOW), PRESET_MODE_OVERLAY_2_START, PRESET_MODE_OVERLAY_2_LENGTH) });
  private final Animation cargo_preset_mode_overlay = new Overlay(new Animation[] {
      new ClippedAnimation(new Solid(Color.RED), PRESET_MODE_OVERLAY_1_START, PRESET_MODE_OVERLAY_1_LENGTH),
      new ClippedAnimation(new Solid(Color.RED), PRESET_MODE_OVERLAY_2_START, PRESET_MODE_OVERLAY_2_LENGTH) });

  public NeoPixels() {
    // Put this in a try-catch because a LED error shouldn't crash the whole robot
    try {
      animator = new Animator();
      coordinator = new AnimationCoordinator(animator);
      setBaseAnimation(rainbow);
      brightnessWidget = RobotMap.outreachTab.add("LED Brightness", 0.4).withPosition(0, 0).withSize(1, 1).getEntry();
      brightnessWidget.addListener(notice -> {
        BrightnessFilter.setBrightness(notice.value.getDouble());
      }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    } catch (Exception e) {
      e.printStackTrace();
      if (animator != null) {
        animator.stopAnimation();
        animator = null;
      }
    }

    Color[] indexColors = new Color[10];
    for (int i = 0; i < 5; i++) {
      indexColors[i] = Color.WHITE;
    }
    for (int i = 5; i < 10; i++) {
      indexColors[i] = Color.BLACK;
    }
    ledIndexer = new Solid(indexColors);
  }

  public void setBaseAnimation(Animation animation) {
    if (coordinator == null)
      return;
    coordinator.popAnimation("Base");
    coordinator.pushAnimation("Base", animation, 0, false);
  }

  public void selectHatchPresetMode() {
    if (coordinator == null)
      return;
    if (!coordinator.hasAnimation("hatch_preset_mode")) {
      coordinator.pushAnimation("hatch_preset_mode", hatch_preset_mode_overlay, 10, true);
      System.out.println("Set hatch preset mode animation");
    }
  }

  public void selectCargoPresetMode() {
    if (coordinator == null)
      return;
    if (!coordinator.hasAnimation("cargo_preset_mode")) {
      coordinator.pushAnimation("cargo_preset_mode", cargo_preset_mode_overlay, 10, true);
      System.out.println("Set cargo preset mode animation");
    }
  }

  public void disabled() {
    if (coordinator == null)
      return;
    if (coordinator.hasAnimation("hatch_preset_mode"))
      coordinator.popAnimation("hatch_preset_mode");
    if (coordinator.hasAnimation("cargo_preset_mode"))
      coordinator.popAnimation("cargo_preset_mode");
  }

  public void brownedOut() {
    if (coordinator == null)
      return;
    coordinator.pushAnimation("Brownout", blinkRed, 1000, false);
  }

}