package net.galacticraft.plugins.tinyforge.tests.versions;

import net.galacticraft.plugins.tinyforge.versions.YarnVersion;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
public class YarnVersionTests {

    @ParameterizedTest
    @ArgumentsSource(YarnVersionArgSource.class)
    public void StaticOf_Should_ParseVersionCorrectly_When_ValidVersionGiven(String mc, String yarn) {
        // Given
        String versionIn = mc + "+build." + yarn;

        // When
        YarnVersion versionOut = YarnVersion.of(versionIn);

        // Then
        assertEquals(mc, versionOut.getMinecraftVersion());
        assertEquals(yarn, versionOut.getYarnBuild());
    }

    @ParameterizedTest
    @ArgumentsSource(InvalidYarnVersionArgSource.class)
    public void StaticOf_Should_ThrowInvalidArg_When_InvalidVersionGiven(String version) {
        // Given
        // @arg: version

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> YarnVersion.of(version));
    }

    private static class YarnVersionArgSource implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            List<String> mcVersions = Arrays.asList(
                    "1.18",
                    "1.18-pre1",
                    "1.18-rc1",
                    "1.18.1",
                    "1.18.1-pre1",
                    "1.18.1-rc1",
                    "22w17a",
                    "22w17b"
            );

            List<Arguments> args = new ArrayList<>();
            for (String mcVersion : mcVersions) {
                IntStream.rangeClosed(0, 100).forEach(i -> args.add(Arguments.of(mcVersion, String.valueOf(i))));
            }
            return args.stream();
        }
    }

    private static class InvalidYarnVersionArgSource implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                    Arguments.of(""),
                    Arguments.of("abajsdbasjbdoasubfobu10823y108263012863"),
                    Arguments.of("asd.asda.sd.ads.asd.asd.as.d.ads."),
                    Arguments.of("I have literally no idea what else to put in here.")
            );
        }
    }
}

