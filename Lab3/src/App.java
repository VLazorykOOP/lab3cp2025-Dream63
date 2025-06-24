public class App {
    public static void main(String[] args) {
        // Builder
        ComputerBuilder builder = new GamingComputerBuilder();
        ComputerConfigurator config = new ComputerConfigurator();

        Computer pc1 = builder.setCPU("Intel i9").setGPU("RTX 4080").setRAM("32GB").build();

        
        config.setComputer(pc1);
        System.out.println("Initial PC: " + config.getComputer());

        // Save state
        ComputerMemento backup = config.save();

        // Change config
        Computer pc2 = builder.setCPU("AMD Ryzen 9").setGPU("RX 7900").setRAM("64GB").build();
        config.setComputer(pc2);
        System.out.println("Modified PC: " + config.getComputer());

        // Restore
        config.restore(backup);
        System.out.println("Restored PC: " + config.getComputer());

        // Bridge
        ComputerPlatform platform = new PC(new LinuxOS());
        platform.setup(config.getComputer());
    }
}

class Computer {
    String cpu;
    String gpu;
    String ram;

    @Override
    public String toString() {
        return "CPU: " + cpu + ", GPU: " + gpu + ", RAM: " + ram;
    }
}

// Builder
interface ComputerBuilder {
    ComputerBuilder setCPU(String cpu);
    ComputerBuilder setGPU(String gpu);
    ComputerBuilder setRAM(String ram);
    Computer build();
}

// ConcreteBuilder
class GamingComputerBuilder implements ComputerBuilder {
    private final Computer computer = new Computer();

    @Override
    public ComputerBuilder setCPU(String cpu) {
        computer.cpu = cpu;
        return this;
    }

    @Override
    public ComputerBuilder setGPU(String gpu) {
        computer.gpu = gpu;
        return this;
    }

    @Override
    public ComputerBuilder setRAM(String ram) {
        computer.ram = ram;
        return this;
    }

    @Override
    public Computer build() {
        return computer;
    }
}

// Implementor
interface OperatingSystem {
    void installDrivers(Computer computer);
}

// ConcreteImplementors
@SuppressWarnings("unused")
class WindowsOS implements OperatingSystem {

    @Override
    public void installDrivers(Computer computer) {
        System.out.println("Installing Windows drivers for: " + computer);
    }
}


class LinuxOS implements OperatingSystem {
    @Override
    public void installDrivers(Computer computer) {
        System.out.println("Installing Linux drivers for: " + computer);
    }
}

// Abstraction
abstract class ComputerPlatform {
    protected OperatingSystem os;

    public ComputerPlatform(OperatingSystem os) {
        this.os = os;
    }

    public abstract void setup(Computer computer);
}

// RefinedAbstraction
class PC extends ComputerPlatform {
    public PC(OperatingSystem os) {
        super(os);
    }

    @Override
    public void setup(Computer computer) {
        os.installDrivers(computer);
    }
}

class ComputerMemento {
    private final Computer state;

    public ComputerMemento(Computer state) {
        // створюємо копію
        this.state = new Computer();
        this.state.cpu = state.cpu;
        this.state.gpu = state.gpu;
        this.state.ram = state.ram;
    }

    public Computer getSavedState() {
        return state;
    }
}

// Originator
class ComputerConfigurator {
    private Computer currentComputer;

    public void setComputer(Computer computer) {
        this.currentComputer = computer;
    }

    public Computer getComputer() {
        return currentComputer;
    }

    public ComputerMemento save() {
        return new ComputerMemento(currentComputer);
    }

    public void restore(ComputerMemento memento) {
        this.currentComputer = memento.getSavedState();
    }
}