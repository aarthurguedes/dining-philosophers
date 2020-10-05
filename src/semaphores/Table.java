package semaphores;

import enums.PhilosopherState;

import java.util.concurrent.Semaphore;

public class Table {
    private Philosopher[] philosophers;
    private int philosophersNumber;
    Semaphore mutex;

    public Table(int philosophersNumber) {
        this.philosophersNumber = philosophersNumber;
        this.philosophers = new Philosopher[philosophersNumber];
        this.mutex = new Semaphore(1);

        for (int i = 0; i < philosophersNumber; i++) {
            this.philosophers[i] = new Philosopher(i, this);
        }
    }

    private int getLeftPosition(int currentPosition) {
        return (currentPosition + philosophersNumber - 1) % philosophersNumber;
    }

    private int getRightPosition(int currentPosition) {
        return (currentPosition + 1) % philosophersNumber;
    }

    private void tryGetForks(int philosopherId) {
        if (this.philosophers[philosopherId].getState().equals(PhilosopherState.HUNGRY)
                && !this.philosophers[getLeftPosition(philosopherId)].getState().equals(PhilosopherState.EATING)
                && !this.philosophers[getRightPosition(philosopherId)].getState().equals(PhilosopherState.EATING)) {
            this.philosophers[philosopherId].setState(PhilosopherState.EATING);

            System.out.println(String.format("O filosofo %d esta comendo", philosopherId));

            this.philosophers[philosopherId].release();
        }
    }

    public void getForks(int philosopherId) {
        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.philosophers[philosopherId].setState(PhilosopherState.HUNGRY);

        System.out.println(String.format("O filosofo %d esta faminto", philosopherId));

        this.tryGetForks(philosopherId);

        this.mutex.release();
        this.philosophers[philosopherId].block();
    }

    public void returnForks(int philosopherId) {
        try {
            this.mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.philosophers[philosopherId].setState(PhilosopherState.THINKING);

        System.out.println(String.format("O filosofo %d esta pensando", philosopherId));

        this.tryGetForks(this.getLeftPosition(philosopherId));
        this.tryGetForks(this.getRightPosition(philosopherId));

        this.mutex.release();
    }
}
