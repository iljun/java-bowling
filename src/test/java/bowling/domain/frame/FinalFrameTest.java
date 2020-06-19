package bowling.domain.frame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FinalFrameTest {

    private Frame createFrame() {
        return new FinalFrame();
    }

    @ParameterizedTest
    @MethodSource("provideCountAndAvailablePlay")
    @DisplayName("동일 frame에 값을 넣을 수 있는지 확인")
    void availablePlayTest(List<Integer> points, boolean availablePlay) {
        Frame frame = createFrame();
        for (Integer point : points) {
            frame.addPoint(point);
        }
        assertThat(frame.availablePlay()).isEqualTo(availablePlay);
    }

    private static Stream<Arguments> provideCountAndAvailablePlay() {
        return Stream.of(
                Arguments.of(Arrays.asList(10), false),
                Arguments.of(Arrays.asList(5, 5), true),
                Arguments.of(Arrays.asList(0, 0), false),
                Arguments.of(Arrays.asList(5, 4), false),
                Arguments.of(Arrays.asList(4), true)
        );
    }

    @ParameterizedTest
    @MethodSource("provideOverflowScore")
    @DisplayName("총 점수 합산이 10이 넘는 경우 Exception")
    void validateScoreTest(int firstPoint, int secondPoint) {
        Frame frame = createFrame();
        frame.addPoint(firstPoint);
        assertThatThrownBy(() -> frame.addPoint(secondPoint))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideOverflowScore() {
        return Stream.of(
                Arguments.of(5, 6),
                Arguments.of(1, 10)
        );
    }

    @ParameterizedTest
    @MethodSource("provideStrikeOrSpare")
    @DisplayName("Strike나 Spare일 경우 한번 더 게임이 가능")
    void afterStrikeOrSparePlayTest(List<Integer> points, int thirdPoint, int totalCount) {
        Frame frame = createFrame();
        for (Integer point : points) {
            frame.addPoint(point);
        }
        frame.addPoint(thirdPoint);
        assertThat(frame.totalScore()).isEqualTo(totalCount);
    }

    private static Stream<Arguments> provideStrikeOrSpare() {
        return Stream.of(
                Arguments.of(Arrays.asList(10), 5, 15),
                Arguments.of(Arrays.asList(5, 5), 5, 15)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNotValidStrikeOrSpare")
    @DisplayName("Strike나 Spare일 경우 총합이 20이 넘는 경우 Exception")
    void validateAddStrikeOrSpare(List<Integer> points, int thirdPoint) {
        Frame frame = createFrame();
        for (Integer point : points) {
            frame.addPoint(point);
        }

        assertThatThrownBy(() -> frame.addPoint(thirdPoint))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> provideNotValidStrikeOrSpare() {
        return Stream.of(
                Arguments.of(Arrays.asList(10), 11),
                Arguments.of(Arrays.asList(5, 5), 11)
        );
    }
}