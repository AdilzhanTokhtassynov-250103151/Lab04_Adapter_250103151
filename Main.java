// ===================== PART 1: PROXY =====================

// ---------- Task 1: Protection Proxy ----------
interface Image {
    void show();
}

class RealImage implements Image {
    private final String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
        System.out.println("--> [SLOW] Reading " + fileName + " from hard drive...");
    }

    @Override
    public void show() {
        System.out.println("Displaying " + fileName + " on screen!");
    }
}

class ProxyImage implements Image {
    private final String fileName;
    private final String userRole;
    private RealImage realImage;

    public ProxyImage(String fileName, String userRole) {
        this.fileName = fileName;
        this.userRole = userRole;
    }

    @Override
    public void show() {
        if (!"ADMIN".equals(userRole)) {
            System.out.println("[ACCESS DENIED] You lack permission to view: " + fileName);
            return;
        }
        if (realImage == null) {
            realImage = new RealImage(fileName); // lazy loading
        }
        realImage.show();
    }
}

// ---------- Task 2: Tracking / Logging Proxy ----------
interface Video {
    void play();
}

class RealVideo implements Video {
    private final String fileName;

    public RealVideo(String fileName) {
        this.fileName = fileName;
        System.out.println("--> [DOWNLOADING] Connecting to server & buffering 4K video: " + fileName);
    }

    @Override
    public void play() {
        System.out.println("Playing video: " + fileName);
    }
}

class ProxyVideo implements Video {
    private final String fileName;
    private RealVideo realVideo;
    private int viewCount = 0;

    public ProxyVideo(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void play() {
        viewCount++;
        System.out.println("--> [AUDIT] Video has been played " + viewCount + " time(s).");
        if (realVideo == null) {
            realVideo = new RealVideo(fileName); // lazy loading on first view
        }
        realVideo.play();
    }
}

// ---------- Task 3: School Wi-Fi Firewall ----------
interface Internet {
    void connectTo(String site);
}

class RealInternet implements Internet {
    @Override
    public void connectTo(String site) {
        System.out.println("Connected to site: " + site);
    }
}

class SchoolProxyInternet implements Internet {
    private RealInternet realInternet;

    @Override
    public void connectTo(String site) {
        if (site.equals("gaming.com") || site.equals("socialmedia.com")) {
            System.out.println("[BLOCKED BY FIREWALL] Cannot access: " + site);
            return;
        }
        if (realInternet == null) {
            realInternet = new RealInternet();
        }
        realInternet.connectTo(site);
    }
}

// ===================== PART 2: BRIDGE =====================

// ---------- Implementor side: devices ----------
interface Device {
    void turnOn();
    void turnOff();
    void setVolume(int level);
}

class Radio implements Device {
    @Override
    public void turnOn() {
        System.out.println("Radio starts playing static/music.");
    }

    @Override
    public void turnOff() {
        System.out.println("Radio turned off.");
    }

    @Override
    public void setVolume(int level) {
        System.out.println("Radio volume knob turned to: " + level);
    }
}

// ---------- Abstraction side: remotes ----------
class BasicRemote {
    protected Device device;

    public BasicRemote(Device device) {
        this.device = device;
    }

    public void powerOn() {
        device.turnOn();
    }

    public void powerOff() {
        device.turnOff();
    }
}

class AdvancedRemote extends BasicRemote {
    public AdvancedRemote(Device device) {
        super(device);
    }

    public void mute() {
        System.out.println("[Remote] Mute button pressed!");
        device.setVolume(0);
    }
}

// ---------- Task 4: new device (zero changes in remotes) ----------
class SmartLight implements Device {
    @Override
    public void turnOn() {
        System.out.println("Smart Light glows bright white.");
    }

    @Override
    public void turnOff() {
        System.out.println("Smart Light shuts off.");
    }

    @Override
    public void setVolume(int level) {
        System.out.println("Smart Light brightness set to: " + level + "%");
    }
}

// ---------- Task 5: new remote (zero changes in devices) ----------
class PartyRemote extends BasicRemote {
    public PartyRemote(Device device) {
        super(device);
    }

    public void partyMode() {
        System.out.println("[Party Remote] Pumping up the party!");
        device.turnOn();
        device.setVolume(100);
    }
}

// ---------- Task 6: RPG Weapon System ----------
interface Weapon {
    void use();
}

class Sword implements Weapon {
    @Override
    public void use() {
        System.out.println("Slash! Dealing 20 physical damage.");
    }
}

class MagicWand implements Weapon {
    @Override
    public void use() {
        System.out.println("Zap! Dealing 35 magic damage.");
    }
}

abstract class Hero {
    protected Weapon weapon; // the bridge

    public Hero(Weapon weapon) {
        this.weapon = weapon;
    }
}

class Knight extends Hero {
    public Knight(Weapon weapon) {
        super(weapon);
    }

    public void attack() {
        weapon.use();
    }
}

class Wizard extends Hero {
    public Wizard(Weapon weapon) {
        super(weapon);
    }

    public void attack() {
        weapon.use();
    }
}

// ===================== MAIN =====================
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Task 1: The Bouncer ===");
        Image guest = new ProxyImage("salary_sheet.png", "GUEST");
        Image admin = new ProxyImage("salary_sheet.png", "ADMIN");
        guest.show();
        admin.show();

        System.out.println("\n=== Task 2: The View Counter ===");
        Video video = new ProxyVideo("Cat_Memes.mp4");
        video.play();
        video.play();
        video.play();

        System.out.println("\n=== Task 3: School Wi-Fi Firewall ===");
        Internet internet = new SchoolProxyInternet();
        internet.connectTo("wikipedia.org");
        internet.connectTo("gaming.com");

        System.out.println("\n=== Task 4: The New Machine ===");
        Device light = new SmartLight();
        AdvancedRemote remote = new AdvancedRemote(light);
        remote.powerOn();
        remote.mute();

        System.out.println("\n=== Task 5: The New Button ===");
        Device radio = new Radio();
        PartyRemote party = new PartyRemote(radio);
        party.partyMode();

        System.out.println("\n=== Task 6: RPG Weapon System ===");
        Knight swordKnight = new Knight(new Sword());
        swordKnight.attack();
        Knight wandKnight = new Knight(new MagicWand());
        wandKnight.attack();
    }
}
