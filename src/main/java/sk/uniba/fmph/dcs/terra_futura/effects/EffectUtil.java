package sk.uniba.fmph.dcs.terra_futura.effects;

import sk.uniba.fmph.dcs.terra_futura.enums.Resource;

import java.util.List;

public final class EffectUtil {
    private static final List<Resource> MATERIALS = List.of(Resource.Green, Resource.Red, Resource.Yellow);
    private EffectUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static boolean containsNonMaterial(final List<Resource> list) {
        return list.stream().anyMatch(resource -> !MATERIALS.contains(resource));
    }
}
