package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

record Move(int count, int from, int to) {
}

public class Main {

    public static void main(String[] args) throws IOException {
        Main main = new Main();
        main.run();
    }

    final String path = "TODO";

    Stack<Character>[] stacks;
    List<Move> moves;

    void run() throws IOException {
        // Read stacks
        readStacks();
        printStacks();

        // Read moves
        readMoves();
        // Execute moves
        this.moves.forEach(this::move);

        printStacks();
    }

    void readStacks() throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(this.path + "start.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] values = line.split(",");

                int stackCount = values.length;
                this.createStack(stackCount);

                for (int i = 0; i < values.length; i++) {
                    final String chr = values[i];
                    if (!chr.equals("")) {
                        this.stacks[i].push(chr.charAt(0));
                    }
                }
            }
        }
    }

    void createStack(final int stackCount) {
        if (this.stacks != null) {
            return;
        }

        this.stacks = (Stack<Character>[]) new Stack[stackCount];
        IntStream.range(0, stackCount).forEach(index -> this.stacks[index] = new Stack<>());
    }

    void readMoves() throws IOException {
        this.moves = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(this.path + "moves.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] values = line.split(",");
                final int count = Integer.parseInt(values[0]);
                final int from = Integer.parseInt(values[1]) - 1;
                final int to = Integer.parseInt(values[2]) - 1;

                Move move = new Move(count, from, to);
                this.moves.add(move);
            }
        }
    }

    void move(final Move move) {
        final Stack<Character> from = this.stacks[move.from()];
        final Stack<Character> to = this.stacks[move.to()];
        IntStream.range(0, move.count())
                .forEach(v -> to.push(from.pop()));

        printMove(move);
        printStacks();
    }

    void printStacks() {
        final int highestStack = Arrays.stream(this.stacks).max(Comparator.comparingInt(Vector::size)).get().size();
        for (int row = highestStack - 1; row >= 0; row--) {

            String line = "";
            for (final Stack<Character> stack : this.stacks) {
                try {
                    line += String.format("%s ", stack.get(row));
                } catch (Exception ex) {
                    /* Ignore me */
                    line += "  ";
                }
            }
            System.out.println(line);
        }
        IntStream.range(0, this.stacks.length).forEach(index -> System.out.printf("%s ", (index + 1)));
        System.out.println();
        printSolution();
        System.out.println("-".repeat(30));
    }

    void printSolution() {
        final String solution = Arrays.stream(this.stacks).map(stack -> {
                try {
                return String.valueOf(stack.peek());
        } catch (Exception ex) {
              return " ";
        }}).collect(Collectors.joining(""));
        System.out.printf("==> %s%n", solution);
    }

    void printMove(final Move move) {
        System.out.println(String.format("move %s from %s to %s", move.count(), move.from() + 1, move.to() + 1));
    }
}