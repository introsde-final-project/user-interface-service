package userInterface.server;

import userInterface.server.endpoint.UserImplementation;
import userInterface.server.model.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by bishruti on 2/6/16.
 */
public class UserInterface {

    public static void main(String[] args) throws Exception {
        UserInterface userInterface = new UserInterface();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        System.out.println("*********************************************");
        System.out.println("    WELCOME TO HEART MONITORING SYSTEM!!!!   ");
        System.out.println("*********************************************");
        System.out.println("Press 1 to View the list of all the users (Admin View).");
        System.out.println("Press 2 to Log in with your id.");
        System.out.println("Press 3 to Create a new user.");
        System.out.println("Press 4 to Delete a user.");
        String action = reader.readLine();
        switch (action) {
            case "1":
                System.out.println("List of all the users: ");
                System.out.println("*********************************************");
                getListOfUser();
                System.out.println("---------------------------------------------");
                System.out.println("Press 10 to Log in a user.");
                System.out.println("Press 11 to Delete a user.");
                String listRedirect = reader.readLine();
                if (listRedirect.equals("10")){
                    System.out.println("Enter User Id: ");
                    String getFromListId = reader.readLine();
                    getUserDetail(Integer.parseInt(getFromListId));
                }
                else if (listRedirect.equals("11")){
                    System.out.println("Enter User Id: ");
                    String deleteFromListId = reader.readLine();
                    deleteUser(Integer.parseInt(deleteFromListId));
                    System.out.println("Successfully deleted user with id: " + deleteFromListId);
                }
                printExitMessage();
                break;

            case "2":
                System.out.println("Enter the user id: ");
                String userId = reader.readLine();
                getUserDetail(Integer.parseInt(userId));

                System.out.println("---------------------------------------------");
                System.out.println("Press 20 to Edit the user personal info");
                System.out.println("Press 21 to Create new measure detail");
                System.out.println("Press 22 to View measure detail");

                String getUserChoice = reader.readLine();
                switch (getUserChoice) {
                    case "20":
                        updateUser(Integer.parseInt(userId));
                        System.out.println("---------------------------------------------");
                        System.out.println("User Successfully Updated!!!!");
                        break;
                    case "21":
                        createUserMeasure(Integer.parseInt(userId));
                        getUserDetail(Integer.parseInt(userId));
                        break;
                    case "22":
                        System.out.println("Enter the measure type: ");
                        String getMeasureHistoryChoice = reader.readLine();
                        getUserHistory(Integer.parseInt(userId), getMeasureHistoryChoice);
                        System.out.println("---------------------------------------------");
                        System.out.println("Press 220 to view the details of a particular measure");
                        System.out.println("Press 221 to update a particular measure");
                        String readMeasureDetail = reader.readLine();
                        if (readMeasureDetail.equals("220")){
                            System.out.println("Enter Health MeasureHistory Id: ");
                            String readMeasureId = reader.readLine();
                            getUserMeasure(Integer.parseInt(userId), getMeasureHistoryChoice, Integer.parseInt(readMeasureId));
                        }
                        else if (readMeasureDetail.equals("221")) {
                            updateUserMeasure(Integer.parseInt(userId));
                        }
//                        getUserDetail(Integer.parseInt(userId));
                        break;
                    default:
                        break;
                }
                printExitMessage();
                break;

            case "3":
                int newUserId = createUser();
                System.out.println("---------------------------------------------");
                System.out.println("Press 30 to go to user profile");
                String createRedirect = reader.readLine();
                if (createRedirect.equals("30")){
                    getUserDetail(newUserId);
                }
                break;

            case "4":
                System.out.println("Enter the user id: ");
                String deleteId = reader.readLine();
                deleteUser(Integer.parseInt(deleteId));
                System.out.println("Successfully deleted user with id: " + deleteId);
                printExitMessage();
                break;

            default:
                System.out.println("*********************************************");
                System.out.println("Exiting from Heart Monitoring System...");
                break;
        }

    }

    /*  Request to obtain all the users and their details in the list.
        Expected Input: -
        Expected Output: List of users (List) */

    public static void getListOfUser() throws Exception {
        List<User> userList = UserImplementation.getListOfUser();
        if (userList == null) {
            System.out.println("There are no Users in this system!!!!");
        }
        else {
            for (User user : userList) {
                printUserInfo(user);
                getUserHealthProfile(user);
            }
        }
    }

    /* Request to obtain a user and the details associated to that user from the list.
       Expected Input: uId (Integer)
       Expected Output: User and the details associated to that user. (Object) */

    public static void getUserDetail(int id) throws Exception {
        User user = UserImplementation.getUserDetail(id);
        if (user == null) {
            System.out.println("User Not Found!!!!");
        }
        else {
            printUserDetails(user);
        }
    }

     /* Request to obtain all measure details about a measure of a user in the list.
        Expected Input: uId (Integer)
                       measureType (String)
       Expected Output: List of details of measure types. (List) */

    public static void getUserHistory(int uId, String measureType) throws Exception {
        List<HealthMeasureHistory> healthMeasureHistory = UserImplementation.getUserHistory(uId, measureType);
        if (healthMeasureHistory == null) {
            System.out.println("Health Measure History is empty!!!");
        }
        else {
            printUserHealthMeasureHistory(healthMeasureHistory);
        }
    }

     /*  Request to obtain measure details about a particular measure of a user in the list.
        Expected Input: uId (Integer)
                        measureType (String)
                        hmhId (Integer)
        Expected Output: Details of a particular measure. (List) */

    public static List<HealthMeasureHistory> getUserMeasure(int uId, String measureType, int hmhId) throws Exception {
        List<HealthMeasureHistory> healthMeasureHistory = UserImplementation.getUserMeasure(uId, measureType, hmhId);
        if (healthMeasureHistory == null) {
            System.out.println("Health Measure History is empty!!!");
            return null;
        }
        else {
            printUserHealthMeasureHistory(healthMeasureHistory);
            return healthMeasureHistory;
        }
    }

    /*  Request to add a new user in the list.
        Expected Input: User (Object)
        Expected Output: Newly created User with the details associated to that user. (Object) */

    public static int createUser() throws Exception {
        System.out.println("*********************************************");
        System.out.println("              Create New User                ");
        System.out.println("*********************************************");
        BufferedReader userReader = new BufferedReader(new InputStreamReader(
                System.in));
        System.out.println("Personal Details");
        System.out.println("---------------------------------------------");
        System.out.println("FirstName: ");
        String firstName = userReader.readLine();
        System.out.println("LastName: ");
        String lastName = userReader.readLine();
        System.out.println("BirthDate (DD/MM/YYYY): ");
        String birthDate = userReader.readLine();
        System.out.println("BloodGroup: ");
        String bloodGroup = userReader.readLine();
        System.out.println("Address: ");
        String address = userReader.readLine();
        System.out.println("---------------------------------------------");
        System.out.println("Health Profile");
        System.out.println("---------------------------------------------");
        System.out.println("Weight (kilogram): ");
        String weight = userReader.readLine();
        System.out.println("Height (meters): ");
        String height = userReader.readLine();
        System.out.println("BMI: ");
        String bmi = userReader.readLine();
        System.out.println("Systolic Bloodpressure: ");
        String systolicBloodpressure = userReader.readLine();
        System.out.println("Diastolic Bloodpressure: ");
        String diastolicBloodpressure = userReader.readLine();
        UserProcess user = new UserProcess();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date birthDay = format.parse(String.valueOf(birthDate));
        user.setBirthDate(birthDay);
        user.setBloodGroup(bloodGroup);
        user.setAddress(address);

        CurrentHealth currentHealth = new CurrentHealth();

        List<HealthProfile> healthProfileList = new ArrayList<HealthProfile>();
        HealthProfile healthProfileWeight = new HealthProfile();
        healthProfileWeight.setMeasureType("weight");
        healthProfileWeight.setMeasureValue(weight);
        healthProfileWeight.setMeasureValueType("String-Float");
        healthProfileList.add(healthProfileWeight);

        HealthProfile healthProfileHeight = new HealthProfile();
        healthProfileHeight.setMeasureType("height");
        healthProfileHeight.setMeasureValue(height);
        healthProfileHeight.setMeasureValueType("String-Float");
        healthProfileList.add(healthProfileHeight);

        /*float weightKg = Float.parseFloat(weight);
        float heightMeter = Float.parseFloat(height);
        float bmi = (float) (weightKg/(Math.pow(heightMeter, 2)));*/
        HealthProfile healthProfileBMI = new HealthProfile();
        healthProfileBMI.setMeasureType("bmi");
        healthProfileBMI.setMeasureValue(bmi);
        healthProfileBMI.setMeasureValueType("String-Float");
        healthProfileList.add(healthProfileBMI);

        HealthProfile healthProfileSysBP = new HealthProfile();
        healthProfileSysBP.setMeasureType("systolic-bloodpressure");
        healthProfileSysBP.setMeasureValue(systolicBloodpressure);
        healthProfileSysBP.setMeasureValueType("String-Float");
        healthProfileList.add(healthProfileSysBP);

        HealthProfile healthProfileDiaBP = new HealthProfile();
        healthProfileDiaBP.setMeasureType("diastolic-bloodpressure");
        healthProfileDiaBP.setMeasureValue(diastolicBloodpressure);
        healthProfileDiaBP.setMeasureValueType("String-Float");
        healthProfileList.add(healthProfileDiaBP);

        currentHealth.setMeasureType(healthProfileList);
        user.setCurrentHealth(currentHealth);
        UserProcess newUser = UserImplementation.createUser(user);
        System.out.println("User successfully Created!!!");
        printUserProcessInfo(newUser);
        getUserProcessHealthProfile(newUser);
        return newUser.getuId();
    }

    /*  Request to edit a user in the list.
        Expected Input: uId (Integer) and User (Object)
        Expected Output: Edited User with the details associated to that user. (Object) */

    public static void updateUser(int userId) throws Exception {
        System.out.println("*********************************************");
        System.out.println("          Edit User Personal Details         ");
        System.out.println("*********************************************");
        BufferedReader userUpdate = new BufferedReader(new InputStreamReader(
                System.in));
        System.out.println("---------------------------------------------");
        System.out.println("FirstName: ");
        String updatedFirstName = userUpdate.readLine();
        System.out.println("LastName: ");
        String updatedLastName = userUpdate.readLine();
        System.out.println(updatedLastName);
        System.out.println("BirthDate (DD/MM/YYYY): ");
        String updatedBirthDate = userUpdate.readLine();
        System.out.println("BloodGroup: ");
        String updatedBloodGroup = userUpdate.readLine();
        System.out.println(updatedBirthDate);
        System.out.println("Address: ");
        String updatedAddress = userUpdate.readLine();
        System.out.println(updatedAddress);

        UserProcess user = new UserProcess();
        user.setFirstName(updatedFirstName);
        user.setLastName(updatedLastName);
        user.setBloodGroup(updatedBloodGroup);
        user.setAddress(updatedAddress);
      if (!updatedBirthDate.isEmpty()) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date birthDay = format.parse(String.valueOf(updatedBirthDate));
            user.setBirthDate(birthDay);
       }

        UserProcess updatedUser = UserImplementation.updateUser(userId, user);
        printUserProcessInfo(updatedUser);
    }

    /*  Request to delete a user from the list.
        Expected Input: uId (Integer)
        Expected Output: Response Message. */

    public static void deleteUser(int userId) throws Exception {
        UserImplementation.deleteUser(userId);
    }

    /* Request to create measure details about a measure of a user in the list.
       Expected Input: uId (Integer)
       measureType (String)
       MeasureDetails (Object)
       Expected Output:
       List of newly created measure. (Object) */

    public static void createUserMeasure(int uId) throws Exception {
        System.out.println("*********************************************");
        System.out.println("             Create New Measure              ");
        System.out.println("*********************************************");

        BufferedReader measureReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Press 210 to create new Weight details");
        System.out.println("Press 211 to create new Height details");
        System.out.println("Press 212 to create new BMI details");
        System.out.println("Press 213 to create new Systolic Bloodpressure details");
        System.out.println("Press 214 to create new Diastolic Bloodpressure details");
        String createMeasureChoice = measureReader.readLine();
        switch (createMeasureChoice) {
            case "210":
                System.out.println("Weight (kilogram): ");
                String createWeightValue = measureReader.readLine();
                HealthMeasureHistory weightHealthMeasureHistory = new HealthMeasureHistory();
                weightHealthMeasureHistory.setMeasureType("weight");
                weightHealthMeasureHistory.setMeasureValue(createWeightValue);
                weightHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> newWeightHealthMeasureHistories = UserImplementation.createUserMeasure(uId, "weight", weightHealthMeasureHistory);
                printUserHealthMeasureHistory(newWeightHealthMeasureHistories);
                break;
            case "211":
                System.out.println("Height (meters): ");
                String createHeightValue = measureReader.readLine();
                HealthMeasureHistory heightHealthMeasureHistory = new HealthMeasureHistory();
                heightHealthMeasureHistory.setMeasureType("height");
                heightHealthMeasureHistory.setMeasureValue(createHeightValue);
                heightHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> newHeightHealthMeasureHistories = UserImplementation.createUserMeasure(uId, "height", heightHealthMeasureHistory);
                printUserHealthMeasureHistory(newHeightHealthMeasureHistories);
                break;
            case "212":
                System.out.println("BMI: ");
                String createBMIValue = measureReader.readLine();
                HealthMeasureHistory bmiHealthMeasureHistory = new HealthMeasureHistory();
                bmiHealthMeasureHistory.setMeasureType("bmi");
                bmiHealthMeasureHistory.setMeasureValue(createBMIValue);
                bmiHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> newBMIHealthMeasureHistories = UserImplementation.createUserMeasure(uId, "bmi", bmiHealthMeasureHistory);
                printUserHealthMeasureHistory(newBMIHealthMeasureHistories);
                break;
            case "213":
                System.out.println("Systolic Bloodpressure: ");
                String createSysBPValue = measureReader.readLine();
                HealthMeasureHistory sysBPHealthMeasureHistory = new HealthMeasureHistory();
                sysBPHealthMeasureHistory.setMeasureType("systolic-bloodpressure");
                sysBPHealthMeasureHistory.setMeasureValue(createSysBPValue);
                sysBPHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> newSysBPHealthMeasureHistories = UserImplementation.createUserMeasure(uId, "systolic-bloodpressure", sysBPHealthMeasureHistory);
                printUserHealthMeasureHistory(newSysBPHealthMeasureHistories);
                break;
            case "214":
                System.out.println("Diastolic Bloodpressure: ");
                String createDiaBPValue = measureReader.readLine();
                HealthMeasureHistory diaBPHealthMeasureHistory = new HealthMeasureHistory();
                diaBPHealthMeasureHistory.setMeasureType("diastolic-bloodpressure");
                diaBPHealthMeasureHistory.setMeasureValue(createDiaBPValue);
                diaBPHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> newDiaBPHealthMeasureHistories = UserImplementation.createUserMeasure(uId, "diastolic-bloodpressure", diaBPHealthMeasureHistory);
                printUserHealthMeasureHistory(newDiaBPHealthMeasureHistories);
                break;
            default:
                break;
        }
    }

    /*  Request to update measure details about a measure of a user in the list.
        Expected Input: uId (Integer)
        measureType (String)
        hmhId (Integer)
        MeasureDetails (Object)
        Expected Output:
        List of updated measure. (Object) */

    public static void updateUserMeasure(int uId) throws Exception {
        System.out.println("*********************************************");
        System.out.println("                Update Measure               ");
        System.out.println("*********************************************");
        BufferedReader measureReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Press 2210 to update Weight details");
        System.out.println("Press 2211 to update Height details");
        System.out.println("Press 2212 to update BMI details");
        System.out.println("Press 2213 to update Systolic Bloodpressure details");
        System.out.println("Press 2214 to update Diastolic Bloodpressure details");
        String updateMeasureChoice = measureReader.readLine();
        System.out.println("Enter Health MeasureHistory Id: ");
        String updateMeasureId = measureReader.readLine();
        switch (updateMeasureChoice) {
            case "2210":
                System.out.println("Weight (kilogram): ");
                String updateWeightValue = measureReader.readLine();
                HealthMeasureHistory updateWeightHealthMeasureHistory = new HealthMeasureHistory();
                updateWeightHealthMeasureHistory.setMeasureType("weight");
                updateWeightHealthMeasureHistory.setMeasureValue(updateWeightValue);
                updateWeightHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> updateWeightHealthMeasureHistories = UserImplementation.updateUserMeasure(uId, "weight", updateWeightHealthMeasureHistory, Integer.parseInt(updateMeasureId));
                printUserHealthMeasureHistory(updateWeightHealthMeasureHistories);
                break;
            case "2211":
                System.out.println("Height (meters): ");
                String updateHeightValue = measureReader.readLine();
                HealthMeasureHistory updateHeightHealthMeasureHistory = new HealthMeasureHistory();
                updateHeightHealthMeasureHistory.setMeasureType("height");
                updateHeightHealthMeasureHistory.setMeasureValue(updateHeightValue);
                updateHeightHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> updateHeightHealthMeasureHistories = UserImplementation.updateUserMeasure(uId, "height", updateHeightHealthMeasureHistory, Integer.parseInt(updateMeasureId));
                printUserHealthMeasureHistory(updateHeightHealthMeasureHistories);
                break;
            case "2212":
                System.out.println("BMI: ");
                String updateBMIValue = measureReader.readLine();
                HealthMeasureHistory updateBMIHealthMeasureHistory = new HealthMeasureHistory();
                updateBMIHealthMeasureHistory.setMeasureType("bmi");
                updateBMIHealthMeasureHistory.setMeasureValue(updateBMIValue);
                updateBMIHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> updateBMIHealthMeasureHistories = UserImplementation.updateUserMeasure(uId, "bmi", updateBMIHealthMeasureHistory, Integer.parseInt(updateMeasureId));
                printUserHealthMeasureHistory(updateBMIHealthMeasureHistories);
                break;
            case "2213":
                System.out.println("Systolic Bloodpressure: ");
                String updateSysBPValue = measureReader.readLine();
                HealthMeasureHistory updateSysBPHealthMeasureHistory = new HealthMeasureHistory();
                updateSysBPHealthMeasureHistory.setMeasureType("systolic-bloodpressure");
                updateSysBPHealthMeasureHistory.setMeasureValue(updateSysBPValue);
                updateSysBPHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> updateSysBPHealthMeasureHistories = UserImplementation.updateUserMeasure(uId, "systolic-bloodpressure", updateSysBPHealthMeasureHistory, Integer.parseInt(updateMeasureId));
                printUserHealthMeasureHistory(updateSysBPHealthMeasureHistories);
                break;
            case "2214":
                System.out.println("Diastolic Bloodpressure: ");
                String updateDiaBPValue = measureReader.readLine();
                HealthMeasureHistory updateDiaBPHealthMeasureHistory = new HealthMeasureHistory();
                updateDiaBPHealthMeasureHistory.setMeasureType("diastolic-bloodpressure");
                updateDiaBPHealthMeasureHistory.setMeasureValue(updateDiaBPValue);
                updateDiaBPHealthMeasureHistory.setMeasureValueType("String-Float");
                List<HealthMeasureHistory> updateDiaBPHealthMeasureHistories = UserImplementation.updateUserMeasure(uId, "diastolic-bloodpressure", updateDiaBPHealthMeasureHistory, Integer.parseInt(updateMeasureId));
                printUserHealthMeasureHistory(updateDiaBPHealthMeasureHistories);
                break;
            default:
                break;
        }
    }

    //**********************************************************************************************************************
    //  HELPER METHODS
    //**********************************************************************************************************************

    public static void printUserInfo(User user) {
        System.out.println("*********************************************");
        System.out.println("Details for user with UID : " + user.getuId());
        System.out.println("*********************************************");
        System.out.println("FirstName   : " + user.getFirstName());
        System.out.println("LastName    : " + user.getLastName());
        System.out.println("BirthDate   : " + user.getBirthDate());
        System.out.println("BloodGroup  : " + user.getBloodGroup());
        System.out.println("Address     : " + user.getAddress());
    }

    public static void printUserProcessInfo(UserProcess user) {
        System.out.println("*********************************************");
        System.out.println("Details for user with UID : " + user.getuId());
        System.out.println("*********************************************");
        System.out.println("FirstName   : " + user.getFirstName());
        System.out.println("LastName    : " + user.getLastName());
        System.out.println("BirthDate   : " + user.getBirthDate());
        System.out.println("BloodGroup  : " + user.getBloodGroup());
        System.out.println("Address     : " + user.getAddress());
    }

    public static void printUserDetails(User user) {
        System.out.println("*********************************************");
        System.out.println("             THOUGHT OF THE DAY              ");
        System.out.println("---------------------------------------------");
        System.out.println(user.getQuote());
        System.out.println("*********************************************");
        System.out.println("Details for user with UID : " + user.getuId());
        System.out.println("---------------------------------------------");
        System.out.println("FirstName   : " + user.getFirstName());
        System.out.println("LastName    : " + user.getLastName());
        System.out.println("BirthDate   : " + user.getBirthDate());
        System.out.println("BloodGroup  : " + user.getBloodGroup());
        System.out.println("Address     : " + user.getAddress());
        getUserHealthProfile(user);
        System.out.println("---------------------------------------------");
        System.out.println("Your Goals must be: ");
        ArrayList<String> goalList = user.getGoal();
        for (String goal: goalList) {
            System.out.println("\t" + goal);
        }
        System.out.println("---------------------------------------------");
        System.out.println("Suggested Activities: ");
        ArrayList<String> activityList = user.getActivity();
        for (String activity: activityList) {
            System.out.println("\t" + activity);
        }
        System.out.println("---------------------------------------------");
        System.out.println("You should try this new recepie... ");
        FoodSuggestion foodSuggestion = user.getFoodSuggestion();
        System.out.println(foodSuggestion.getLabel().toUpperCase());
        System.out.println("To make this, all you need is: ");
        ArrayList<String> ingredients = foodSuggestion.getIngredientLines();
        for (String ingredient: ingredients) {
            System.out.println("\t" + ingredient);
        }
        System.out.println("This recepie is good for your health since it is...");
        ArrayList<String> healthLabels = foodSuggestion.getHealthLabels();
        for (String healthLabel: healthLabels) {
            System.out.println("\t" + healthLabel);
        }
        System.out.println("You should definitely try this!!!!");
    }

    public static void getUserHealthProfile(User user) {
        List<HealthProfile> healthProfiles = user.getCurrentHealth().getMeasureType();
        for (HealthProfile healthProfile : healthProfiles) {
            printHealthProfile(healthProfile);
        }
    }

    public static void getUserProcessHealthProfile(UserProcess user) {
        List<HealthProfile> healthProfiles = user.getCurrentHealth().getMeasureType();
        for (HealthProfile healthProfile : healthProfiles) {
            printHealthProfile(healthProfile);
        }
    }

    public static void printHealthProfile(HealthProfile healthProfile) {
        System.out.println("---------------------------------------------");
        System.out.println("HealthProfileId  :" + healthProfile.getIdHealthProfile());
        System.out.println(healthProfile.getMeasureType() +" : "+ healthProfile.getMeasureValue());
        System.out.println("Date Registered  :" + healthProfile.getDateRegistered());
    }

    public static void printUserHealthMeasureHistory(List<HealthMeasureHistory> healthMeasureHistories) {
        for (HealthMeasureHistory healthMeasureHistory : healthMeasureHistories) {
            printHealthMeasureHistory(healthMeasureHistory);
        }
    }

    public static void printHealthMeasureHistory(HealthMeasureHistory healthMeasureHistory) {
        System.out.println("---------------------------------------------");
        System.out.println("HealthMeasureHistoryId  :" + healthMeasureHistory.getHmhId());
        System.out.println(healthMeasureHistory.getMeasureType() +" : "+ healthMeasureHistory.getMeasureValue());
        System.out.println("Date Registered  :" + healthMeasureHistory.getDateRegistered());
    }

    public static void printExitMessage() {
        System.out.println("*********************************************");
        System.out.println("Exiting from Heart Monitoring System...");
    }
}
