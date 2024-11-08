/**
 * ------------------------------------------------------------------------
 * Ex-Wife Chronicles: Gas Leak Edition
 * Level 3 programming project
 *
 * by Jonty Doorman
 *
 * The game is a single-player, adventure puzzle game where the player is trapped in a house.
 * The player must explore the house, interacting with the rooms in order find the necessary items to escape.
 * The player is timed by a gas leakage and therefore must manage their time wisely while exploring the map.
 * The player wins if they are able to escape the house before the time runs out and loses if the time does run out.
 * ------------------------------------------------------------------------
 */


import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import java.awt.*
import java.awt.event.*
import javax.swing.*

//=============================================================================================

// Creates the location class

class Location(val name: String, val description: String) {
    var up: Location? = null
    var right: Location? = null
    var down: Location? = null
    var left: Location? = null
    var item: Item? = null
    var locked: Boolean = false

    // Locks and unlocks locations
    fun lock(){
        locked = true
    }
    fun unlock(){
        locked = false
    }

    // Allows the locations to be connected
    fun addUp(location: Location) {
        if (up == null) {
            up = location
            location.addDown(this)
        }
    }

    fun addDown(location: Location) {
        if (down == null) {
            down = location
            location.addUp(this)
        }
    }

    fun addLeft(location: Location) {
        if (left == null) {
            left = location
            location.addRight(this)
        }
    }

    fun addRight(location: Location) {
        if (right == null) {
            right = location
            location.addLeft(this)
        }
    }
    // Adds and removes items from locations
    fun addItem(newItem: Item) {
        item = newItem
    }

    fun removeItem() {
        item = null
    }
    }

// Item class
class Item(val name: String){
    override fun toString(): String{
        return "$name"}

}

/**
 * GUI class
 * Defines the UI and responds to events
 */
class GUI : JFrame(), ActionListener {

    val locations = mutableListOf<Location>()
    var currentLocation: Location
    var gas = 0
    // Setup inventory
    val inventory = DefaultListModel<Item>()


    // Setup some properties to hold the UI elements
    private lateinit var locationTitle: JLabel
    private lateinit var locationDesc: JLabel
    private lateinit var inventoryList: JList<Item>
    private lateinit var actionDesc: JLabel
    private lateinit var gasTimer: JLabel

    private lateinit var upButton: JButton
    private lateinit var downButton: JButton
    private lateinit var leftButton: JButton
    private lateinit var rightButton: JButton
    private lateinit var actionButton: JButton


    /**
     * Create, build and run the UI
     */
    init {
        setupWindow()
        setupWorld()
        buildUI()

        currentLocation = locations.first()

        updateLocation()
        // Show the app, centred on screen
        setLocationRelativeTo(null)
        isVisible = true
        gasLeakage()
    }
    // Creates locations
    private fun setupWorld() {
        val start = Location("Entrance", "You're in the entrance of the house. Behind you is the front door, the only exit in the house. You need to find some way to open it...")
        val hallway = Location("Start of Hallway", "A slim hallway that stretches down further. There are 2 doors on either side of you.")
        val hallway1 = Location("End of Hallway", "The end of the hallway. There are rooms in every direction around you.")
        val toilet = Location("Toilet", "A small room with a porcelain floor and walls. A white ceramic sink is on the wall to your left, the toilet sits alone at the end of the room. There doesn't seem to be any running water.")
        val kitchen = Location("Kitchen", "The kitchen has a grey granite bench top and the cabinets and drawers are made of black hardwood giving a very modern feel. The kitchen seems to be barren of any utensils, cutlery, and plates.")
        val dining = Location("Dining Room", "The dining room has a large round wooden table in the middle of the room surrounded by 4 cushioned wooden chairs. A small chandelier hangs above the middle of the table.")
        val laundry = Location("Laundry", "A small laundry room with a laundry machine, dryer, and stainless steel sink. The room feels strangely empty for a such a cramped space.")
        val shower = Location("Bathroom", "The bathroom is on the larger side. It has a white porcelain sink and bathtub. Above the bathtub is a shower head and an off-white curtain. The water doesn't seem to be working.")
        val bed = Location("Small Bedroom", "A small bedroom with a bunk bed in the far corner. There are 2 small desks on the opposite side of the room.")
        val play = Location("Playroom", "A room in a state of disarray. Toys are scattered all over the floor, ranging from stuffed toys to sport balls. One of the only rooms that isn't eerily empty.")
        val master = Location("Master Bedroom", "A bedroom with a king size bed in the middle of the room. The bed has a night stand on either side. One of the draws is ajar and has something poking out the top.")
        val garage = Location("Garage", "The garage has a large empty space where a car would normally reside. There is a tool shelf on the right side of the room.")
        val living = Location("Living Room", "The living room has a tv on the wall and 2 couches facing its direction. The fireplace on the side gives the room a warm, cozy ambience. A fire axe is wall mounted above the fire place behind glass with the sticker 'break incase of emergency'. A seemingly unintuitive place to have it.")
        val end = Location("Front", "")
        val lose = Location("You Lose!", "The gas has filled your lungs.")

        // Puts locations in a list
        locations.add(start)
        locations.add(hallway)
        locations.add(hallway1)
        locations.add(toilet)
        locations.add(kitchen)
        locations.add(dining)
        locations.add(laundry)
        locations.add(shower)
        locations.add(bed)
        locations.add(play)
        locations.add(master)
        locations.add(garage)
        locations.add(living)
        locations.add(end)
        locations.add(lose)

        // Locks the garage at front doors
        garage.lock()
        end.lock()

        // Connects the locations
        start.addUp(hallway)
        start.addDown(end)
        start.addLeft(garage)
        start.addRight(toilet)
        hallway.addUp(hallway1)
        hallway.addLeft(kitchen)
        hallway.addRight(play)
        hallway1.addUp(bed)
        hallway1.addLeft(shower)
        hallway1.addRight(master)
        kitchen.addLeft(dining)
        dining.addUp(laundry)
        dining.addDown(living)
        shower.addLeft(laundry)

        // Creates the items
        val keys = Item("Keys")
        val hammer = Item("Hammer")
        val axe = Item("Fire Axe")
        master.addItem(keys)
        garage.addItem(hammer)
        living.addItem(axe)
    }

    /**
     * Configure the main window
     */
    private fun setupWindow() {
        title = "Ex-Wife Chronicles: Gas Leak Edition"
        contentPane.preferredSize = Dimension(500, 600)
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        isResizable = false
        layout = null

        pack()
    }

    /**
     * Populate the UI
     */
    private fun buildUI() {
        val baseFont = Font(Font.SANS_SERIF, Font.PLAIN, 20)

        locationTitle = JLabel("Location Name", SwingConstants.CENTER)
        locationTitle.bounds = Rectangle(20, 0, 461, 106)
        locationTitle.font = baseFont
        add(locationTitle)

        locationDesc = JLabel("Location Description", SwingConstants.CENTER)
        locationDesc.bounds = Rectangle(60, 130, 380, 320)
        locationDesc.font = baseFont
        add(locationDesc)

        actionDesc = JLabel("", SwingConstants.CENTER)
        actionDesc.bounds = Rectangle(60, 80, 380, 106)
        actionDesc.font = baseFont
        add(actionDesc)

        inventoryList = JList(inventory)
        inventoryList.bounds = Rectangle(0, 510, 190, 100)
        inventoryList.font = baseFont
        add(inventoryList)

        gasTimer = JLabel ("", SwingConstants.CENTER)
        gasTimer.bounds = Rectangle(305, 510, 200, 100)
        gasTimer.font = baseFont
        add(gasTimer)

        upButton = JButton("⇧")
        upButton.bounds = Rectangle(206,400,90,47)
        upButton.font = baseFont
        upButton.addActionListener(this)
        add(upButton)

        downButton = JButton("⇩")
        downButton.bounds = Rectangle(206,504,90,47)
        downButton.font = baseFont
        downButton.addActionListener(this)
        add(downButton)

        leftButton = JButton("⇦")
        leftButton.bounds = Rectangle(92,452,90,47)
        leftButton.font = baseFont
        leftButton.addActionListener(this)
        add(leftButton)

        rightButton = JButton("⇨")
        rightButton.bounds = Rectangle(320,452,90,47)
        rightButton.font = baseFont
        rightButton.addActionListener(this)
        add(rightButton)

        actionButton = JButton("Interact")
        actionButton.bounds = Rectangle(186,452,130,47)
        actionButton.font = baseFont
        actionButton.addActionListener(this)
        add(actionButton)

    }

    /**
     * Handle any UI events
     */
    override fun actionPerformed(e: ActionEvent?) {
        when (e?.source) {
            upButton -> walkUp()
            downButton -> walkDown()
            leftButton -> walkLeft()
            rightButton -> walkRight()
            actionButton -> interact()
        }
    }
    /**
     * Checks if there is an item in the room,
     * Checks if you have hammer before giving the axe
      */
    private fun interact(){
        if (currentLocation.item != null) {
            if(currentLocation.item!!.name == "Fire Axe") {
                if (inventory.size == 2) {
                    actionDesc.text = "<html>You smash the glass with the hammer and retrieve the fire axe!</html>"
                    inventory.addElement(currentLocation.item!!)
                    currentLocation.removeItem()
                }
                else{
                    actionDesc.text = "<html>You attempt to smash the glass but the gas has taken a toll on your body! Maybe find something that can smash it.</html>"
                }
            }
            else {
                actionDesc.text = "You found the ${currentLocation.item!!.name}!"
                inventory.addElement(currentLocation.item!!)
                currentLocation.removeItem()
            }
        }
        else {
            actionDesc.text = "There is nothing useful in this room."
        }
    }
    // Timer that triggers a loss when it reaches 100
    private fun gasLeakage() {
        while (gas <= 100) {
            if (currentLocation.name == "Front"){
                gasTimer.text = ""
            }
            else {
                gas += 1
                Thread.sleep(1000)
                gasTimer.text = "Gas Pollution: $gas"

                if (gas >= 100) {
                    gasTimer.text = ""
                    currentLocation = locations.last()
                    updateLocation()
                    actionButton.isEnabled = false
                    inventory.size = 0
                    actionDesc.text = ""
                }
            }
        }
    }
    /**
     * Checks rooms
     */
    private fun checkUp(){
        if(currentLocation.up != null) {
            upButton.isEnabled = true
        }
        else {
            upButton.isEnabled = false
        }
    }

    private fun checkDown(){
        if(currentLocation.down != null) {
            downButton.isEnabled = true
        }
        else {
            downButton.isEnabled = false
        }
    }

    private fun checkLeft(){
        if(currentLocation.left != null) {
            leftButton.isEnabled = true
        }
        else {
            leftButton.isEnabled = false
        }
    }

    private fun checkRight(){
        if(currentLocation.right != null) {
            rightButton.isEnabled = true
        }
        else {
            rightButton.isEnabled = false
        }
    }

    private fun walkUp() {
            currentLocation = currentLocation.up!!
            updateLocation()
    }
    // Checks if doors are locked
    private fun walkDown() {
        if(currentLocation.down!!.locked) {
            actionDesc.text = "This door is locked"
            if(inventory.size == 3){
                currentLocation.down!!.unlock()
                actionDesc.text = "You unlocked the ${currentLocation.down!!.name} door!"
            }
        }
        else{
            currentLocation = currentLocation.down!!
            updateLocation()
        }

    }

    private fun walkLeft() {
        if(currentLocation.left!!.locked) {
            actionDesc.text = "This door is locked"
            if(inventory.isEmpty == false){
                currentLocation.left!!.unlock()
                actionDesc.text = "You unlocked the ${currentLocation.left!!.name} door!"
            }
        }
        else{
            currentLocation = currentLocation.left!!
            updateLocation()
        }
    }

    private fun walkRight() {
            currentLocation = currentLocation.right!!
            updateLocation()
        }

    private fun updateLocation(){
        locationTitle.text = currentLocation.name
        locationDesc.text = "<html>${currentLocation.description}</html>"
        // Triggers the win when player reaches end
        if (currentLocation.name == "Front") {
            locationTitle.text = "Congratulations!"
            locationDesc.text = "<html>You breathe a sigh of fresh air and the strength slowly returns to your body. You have escaped the gas leakage!</html>"
            upButton.isEnabled = false
            downButton.isEnabled = false
            leftButton.isEnabled = false
            rightButton.isEnabled = false
            actionButton.isEnabled = false
            inventory.size = 0
            actionDesc.text = ""

        }
        // Checks the connected rooms for updated location and resets the interact description
        else {
            checkUp()
            checkDown()
            checkLeft()
            checkRight()
            actionDesc.text = ""
        }
    }
}


//=============================================================================================

/**
 * Launch the application
 */
fun main() {
    // Flat, Dark look-and-feel
    FlatDarkLaf.setup()

    // Create the UI
    GUI()
}


