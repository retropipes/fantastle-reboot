package com.puttysoftware.fantastlereboot;

import com.puttysoftware.diane.ErrorHandler;
import com.puttysoftware.diane.gui.CommonDialogs;
import com.puttysoftware.errorlogger.ErrorLogger;
import com.puttysoftware.fantastlereboot.assets.SoundGroup;
import com.puttysoftware.fantastlereboot.assets.SoundIndex;
import com.puttysoftware.fantastlereboot.loaders.SoundPlayer;

final class GameErrorHandler implements ErrorHandler {
  private static final String LOG_NAME = "FantastleReboot";
  private static final String ERROR_MESSAGE = "An unrecoverable error has occurred.\n"
      + "The details have been recorded.\n" + "The program will now exit.";
  private static final String ERROR_TITLE = "FATAL ERROR";
  private static final String WARNING_MESSAGE = "Something has gone wrong.\n"
      + "The details have been recorded.";
  private static final String WARNING_TITLE = "Warning";
  private ErrorLogger logger;

  GameErrorHandler() {
    this.logger = new ErrorLogger(GameErrorHandler.LOG_NAME);
  }

  @Override
  public void uncaughtException(Thread inT, Throwable inE) {
    this.handle(inE);
  }

  void handle(final Throwable inE) {
    if (inE instanceof RuntimeException) {
      try {
        this.logWarning(inE);
      } catch (final Throwable inE2) {
        inE.addSuppressed(inE2);
        this.logWarningDirectly(inE);
      }
    } else {
      try {
        this.logError(inE);
      } catch (final Throwable inE2) {
        inE.addSuppressed(inE2);
        this.logErrorDirectly(inE);
      }
    }
  }

  void silentlyLog(final Throwable inE) {
    this.logger.logWarning(inE);
  }

  private void logError(final Throwable t) {
    SoundPlayer.playSound(SoundIndex.FATAL, SoundGroup.USER_INTERFACE);
    CommonDialogs.showErrorDialog(GameErrorHandler.ERROR_MESSAGE,
        GameErrorHandler.ERROR_TITLE);
    this.logger.logError(t);
  }

  private void logErrorDirectly(final Throwable t) {
    this.logger.logError(t);
  }

  private void logWarning(final Throwable t) {
    SoundPlayer.playSound(SoundIndex.ALERT, SoundGroup.USER_INTERFACE);
    CommonDialogs.showErrorDialog(GameErrorHandler.WARNING_MESSAGE,
        GameErrorHandler.WARNING_TITLE);
    this.logger.logWarning(t);
  }

  private void logWarningDirectly(final Throwable t) {
    this.logger.logWarning(t);
  }

  void handleWithMessage(final Throwable inE, final String msg) {
    if (inE instanceof RuntimeException) {
      try {
        this.logWarningWithMessage(inE, msg);
      } catch (final Throwable inE2) {
        inE.addSuppressed(inE2);
        this.logWarningDirectly(inE);
      }
    } else {
      try {
        this.logErrorWithMessage(inE, msg);
      } catch (final Throwable inE2) {
        inE.addSuppressed(inE2);
        this.logErrorDirectly(inE);
      }
    }
  }

  private void logErrorWithMessage(final Throwable t, final String msg) {
    SoundPlayer.playSound(SoundIndex.FATAL, SoundGroup.USER_INTERFACE);
    CommonDialogs.showErrorDialog(msg, GameErrorHandler.ERROR_TITLE);
    this.logger.logError(t);
  }

  private void logWarningWithMessage(final Throwable t, final String msg) {
    SoundPlayer.playSound(SoundIndex.ALERT, SoundGroup.USER_INTERFACE);
    CommonDialogs.showErrorDialog(msg, GameErrorHandler.WARNING_TITLE);
    this.logger.logWarning(t);
  }
}
