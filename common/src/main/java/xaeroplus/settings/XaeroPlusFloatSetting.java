package xaeroplus.settings;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.client.KeyMapping;
import xaeroplus.XaeroPlus;
import xaeroplus.settings.XaeroPlusSettingsReflectionHax.SettingLocation;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static java.util.Objects.nonNull;

public class XaeroPlusFloatSetting extends XaeroPlusSetting {
    private final float valueMin;
    private final float valueMax;
    private final float valueStep;

    private float value;
    private FloatConsumer settingChangeConsumer;

    private XaeroPlusFloatSetting(final String settingName,
                                  final String settingNameTranslationKey,
                                  final String tooltipTranslationKey,
                                  final KeyMapping keyBinding,
                                  final float valueMin,
                                  final float valueMax,
                                  final float valueStep,
                                  final float defaultValue,
                                  final FloatConsumer settingChangeConsumer,
                                  final BooleanSupplier visibilitySupplier) {
        super(settingName, settingNameTranslationKey, tooltipTranslationKey, keyBinding, visibilitySupplier);
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.valueStep = valueStep;
        this.value = defaultValue;
        this.settingChangeConsumer = settingChangeConsumer;
    }

    public static XaeroPlusFloatSetting create(String settingName,
                                               String settingNameTranslationKey,
                                               String tooltipTranslationKey,
                                               float valueMin,
                                               float valueMax,
                                               float valueStep,
                                               float defaultValue,
                                               SettingLocation settingLocation) {
        final XaeroPlusFloatSetting setting = new XaeroPlusFloatSetting(
            SETTING_PREFIX + settingName,
            settingNameTranslationKey,
            tooltipTranslationKey,
            null,
            valueMin, valueMax, valueStep, defaultValue, null, null
        );
        settingLocation.getSettingsList().add(setting);
        return setting;
    }

    public static XaeroPlusFloatSetting create(String settingName,
                                               String settingNameTranslationKey,
                                               String tooltipTranslationKey,
                                               float valueMin,
                                               float valueMax,
                                               float valueStep,
                                               float defaultValue,
                                               FloatConsumer changeConsumer,
                                               SettingLocation settingLocation) {
        final XaeroPlusFloatSetting setting = new XaeroPlusFloatSetting(
            SETTING_PREFIX + settingName,
            settingNameTranslationKey,
            tooltipTranslationKey,
            null,
            valueMin, valueMax, valueStep, defaultValue, changeConsumer, null
        );
        settingLocation.getSettingsList().add(setting);
        return setting;
    }

    public static XaeroPlusFloatSetting create(String settingName,
                                               String settingNameTranslationKey,
                                               String tooltipTranslationKey,
                                               float valueMin,
                                               float valueMax,
                                               float valueStep,
                                               float defaultValue,
                                               FloatConsumer changeConsumer,
                                               BooleanSupplier visibilitySupplier,
                                               SettingLocation settingLocation) {
        final XaeroPlusFloatSetting setting = new XaeroPlusFloatSetting(
            SETTING_PREFIX + settingName,
            settingNameTranslationKey,
            tooltipTranslationKey,
            null,
            valueMin, valueMax, valueStep, defaultValue, changeConsumer, visibilitySupplier
        );
        settingLocation.getSettingsList().add(setting);
        return setting;
    }

    public float getValueMin() {
        return valueMin;
    }

    public float getValueMax() {
        return valueMax;
    }

    public float getValueStep() {
        return valueStep;
    }

    public float getValue() {
        return value;
    }

    public void setValue(final float value) {
        this.value = value;
        if (nonNull(getSettingChangeConsumer())) {
            try {
                getSettingChangeConsumer().accept(value);
            } catch (final Exception e) {
                XaeroPlus.LOGGER.warn("Error applying setting change consumer for {}", getSettingName(), e);
            }
        }
    }

    public Consumer<Float> getSettingChangeConsumer() {
        return settingChangeConsumer;
    }

    public void setSettingChangeConsumer(final FloatConsumer settingChangeConsumer) {
        this.settingChangeConsumer = settingChangeConsumer;
    }
    public void init() {
        if (nonNull(settingChangeConsumer)) {
            settingChangeConsumer.accept(value);
        }
    }
}
