import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

abstract class Contact implements Comparable<Contact>
{
    protected String date;

    public Contact(String date)
    {
        this.date=date;
    }

    public boolean isNewerThan(Contact c)
    {
        /*if(*/return this.date.compareTo(c.date)>0;//)
        /*{
            return true;
        }

        else
        {
            return false;
        }*/
    }

    public String getType()
    {
        return "";
    }

    public int compareTo(Contact rhs)
    {
        return -this.date.compareTo(rhs.date);
    }
}

class EmailContact extends Contact
{
    private String email;

    public EmailContact(String date, String email)
    {
        super(date);
        this.email=email;
    }

    String getEmail()
    {
        return this.email;
    }

    @Override

    public String getType()
    {
        return "Email";
    }

    @Override

    public int compareTo(Contact rhs)
    {
        return super.compareTo(rhs);
    }

}

class PhoneContact extends Contact
{
    private String phone;
    public enum Operator{ VIP, ONE, TMOBILE};
    private Operator operator;

    public PhoneContact(String date, String phone)
    {
        super(date);

        this.phone=phone;
        String operatorCode=phone.substring(2,3);
        if(operatorCode.equals("0") || operatorCode.equals("1") || operatorCode.equals("2") )
        {
            this.operator=Operator.TMOBILE;
        }

        else if(operatorCode.equals("5") || operatorCode.equals("6"))
        {
            this.operator=Operator.ONE;
        }

        else if(operatorCode.equals("7") || operatorCode.equals("8"))
        {
            this.operator=Operator.VIP;
        }
    }

    public String getPhone()
    {
        return this.phone;
    }

    public Operator getOperator()
    {
        return this.operator;
    }

    @Override

    public String getType()
    {
        return "Phone";
    }

    @Override

    public int compareTo(Contact rhs)
    {
        return super.compareTo(rhs);
    }

}

class Student
{
    private String firstName, lastName, city;
    private Long index;
    private Integer age;
    private ArrayList<Contact> contacts;

    public Student()
    {

    }

    public Student(String firstName, String lastName, String city, int age, long index)
    {
        this.firstName=firstName;
        this.lastName=lastName;
        this.city=city;
        this.age=age;
        this.index=index;
        this.contacts=new ArrayList<Contact>();
    }

    public void addEmailContact(String date, String email)
    {
        this.contacts.add(new EmailContact(date, email));
    }

    public void addPhoneContact(String date, String phone)
    {
        this.contacts.add(new PhoneContact(date, phone));
    }

    public Contact[] getEmailContacts()
    {
        int size=0;
        for(Contact contact: this.contacts)
        {
            if (contact.getType().equals("Email"))
            {
                size++;
            }
        }

        Contact[] temp=new Contact[size];
        int i=0;
        for(Contact contact: this.contacts)
        {
            if(contact.getType().equals("Email"))
            {
                temp[i]=contact;
                i++;
            }
        }

        return temp;

    }

    public Contact[] getPhoneContacts()
    {
        int size=0;
        for(Contact contact: this.contacts)
        {
            if (contact.getType().equals("Phone"))
            {
                size++;
            }
        }

        Contact[] temp=new Contact[size];
        int i=0;
        for(Contact contact: this.contacts)
        {
            if(contact.getType().equals("Phone"))
            {
                temp[i]=contact;
                i++;
            }
        }

        return temp;

    }

    public Contact getLatestContact()
    {
        Contact temp=this.contacts.get(0);
        for(Contact contact: this.contacts)
        {
            if(temp.compareTo(contact)>0)
            {
                temp=contact;
            }
        }

        return temp;
    }

    public int getContactSize()
    {
        return this.contacts.size();
    }

    public String getCity()
    {
        return this.city;
    }

    public String getFullName()
    {
        return this.firstName+" "+this.lastName;
    }

    public long getIndex()
    {
        return this.index;
    }

    @Override
    public String toString() {
        StringBuilder s=new StringBuilder();
        s.append("{\"ime\":\"");
        s.append(this.firstName);
        s.append("\", \"prezime\":\"");
        s.append(this.lastName);
        s.append("\", \"vozrast\":");
        s.append(this.age);
        s.append(", \"grad\":\"");
        s.append(this.city);
        s.append("\", \"indeks\":");
        s.append(this.index);
        s.append(", \"telefonskiKontakti\":[");
        
        boolean delete=false;
        
        for(Contact contact: this.contacts)
        {
            if(contact.getType().equals("Phone"))
            {
                s.append("\"");
                PhoneContact temp=(PhoneContact)contact;
                s.append(temp.getPhone());
                s.append("\", ");
                if(!delete)
                {
                    delete=true;
                }
            }
        }
        
        if(delete)
        {
            s.deleteCharAt(s.length()-1);
            s.deleteCharAt(s.length()-1);
            delete=false;
        }
        s.append("], \"emailKontakti\":[");
        
        for(Contact contact: this.contacts)
        {
            if(contact.getType().equals("Email"))
            {
                s.append("\"");
                EmailContact temp=(EmailContact)contact;
                s.append(temp.getEmail());
                s.append("\", ");
                if(!delete)
                {
                    delete=true;
                }
            }
        }
        
        
        if(delete)
        
        {
            s.deleteCharAt(s.length()-1);
        	s.deleteCharAt(s.length()-1);
        }
        
        s.append("]}");
        
        return s.toString();
    }
}

class Faculty
{
    private String name;
    private ArrayList<Student> students;

    public Faculty()
    {

    }

    public Faculty(String name, Student[] students)
    {
        this();
        this.name=name;
        this.students=new ArrayList<Student>();
        this.students.addAll(Arrays.asList(students));
        /*for(int i=0; i<students.length; i++)
        {
            this.students.add(students[i]);
        }*/
    }

    public int countStudentsFromCity(String cityName)
    {
        int count=0;
        for(Student student: this.students)
        {
            if(student.getCity().equals(cityName))
            {
                count++;
            }
        }

        return count;
    }

    public Student getStudent(long index)
    {
        Student result=null;
        for(Student student: this.students)
        {
            if(student.getIndex()==index)
            {
                result=student;
                break;
            }
        }

        return result;
    }

    public double getAverageNumberOfContacts()
    {
        double result=0.0;
        for(Student student: this.students)
        {
            result+=(double)student.getContactSize();
        }

        return result/(double)this.students.size();
    }

    public Student getStudentWithMostContacts()
    {
        if(this.students.size()==0)
        {
            return null;
        }
        Student max=this.students.get(0);
        for(Student student: this.students)
        {
            if(student.getContactSize()>max.getContactSize())
            {
                max=student;
            }

            else if(student.getContactSize()==max.getContactSize())
            {
                if(student.getIndex()>max.getIndex())
                {
                    max=student;
                }
            }
        }

        return max;
    }

    @Override
    public String toString() {
        StringBuilder s=new StringBuilder();
        s.append("{\"fakultet\":\"");
        s.append(this.name);
        s.append("\", \"studenti\":[");
        for(Student student: this.students)
        {
            String appended=student.toString();
            appended.replace("{", "]");
            appended.replace("}", "]");
            s.append(appended);
            s.append(", ");
        }
        
        if(this.students.size()>0)
        {
            s.deleteCharAt(s.length()-1);
            s.deleteCharAt(s.length()-1);
        }
        
        s.append("]}");
        
        return s.toString();
    }
}

public class ContactsTester {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        Faculty faculty = null;

        int rvalue = 0;
        long rindex = -1;

        DecimalFormat df = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            rvalue++;
            String operation = scanner.next();

            switch (operation) {
                case "CREATE_FACULTY": {
                    String name = scanner.nextLine().trim();
                    int N = scanner.nextInt();

                    Student[] students = new Student[N];

                    for (int i = 0; i < N; i++) {
                        rvalue++;

                        String firstName = scanner.next();
                        String lastName = scanner.next();
                        String city = scanner.next();
                        int age = scanner.nextInt();
                        long index = scanner.nextLong();

                        if ((rindex == -1) || (rvalue % 13 == 0))
                            rindex = index;

                        Student student = new Student(firstName, lastName, city,
                                age, index);
                        students[i] = student;
                    }

                    faculty = new Faculty(name, students);
                    break;
                }

                case "ADD_EMAIL_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String email = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addEmailContact(date, email);
                    break;
                }

                case "ADD_PHONE_CONTACT": {
                    long index = scanner.nextInt();
                    String date = scanner.next();
                    String phone = scanner.next();

                    rvalue++;

                    if ((rindex == -1) || (rvalue % 3 == 0))
                        rindex = index;

                    faculty.getStudent(index).addPhoneContact(date, phone);
                    break;
                }

                case "CHECK_SIMPLE": {
                    System.out.println("Average number of contacts: "
                            + df.format(faculty.getAverageNumberOfContacts()));

                    rvalue++;

                    String city = faculty.getStudent(rindex).getCity();
                    System.out.println("Number of students from " + city + ": "
                            + faculty.countStudentsFromCity(city));

                    break;
                }

                case "CHECK_DATES": {

                    rvalue++;

                    System.out.print("Latest contact: ");
                    Contact latestContact = faculty.getStudent(rindex)
                            .getLatestContact();
                    if (latestContact.getType().equals("Email"))
                        System.out.println(((EmailContact) latestContact)
                                .getEmail());
                    if (latestContact.getType().equals("Phone"))
                        System.out.println(((PhoneContact) latestContact)
                                .getPhone()
                                + " ("
                                + ((PhoneContact) latestContact).getOperator()
                                .toString() + ")");

                    if (faculty.getStudent(rindex).getEmailContacts().length > 0&&faculty.getStudent(rindex).getPhoneContacts().length > 0) {
                        System.out.print("Number of email and phone contacts: ");
                        System.out
                                .println(faculty.getStudent(rindex)
                                        .getEmailContacts().length
                                        + " "
                                        + faculty.getStudent(rindex)
                                        .getPhoneContacts().length);

                        System.out.print("Comparing dates: ");
                        int posEmail = rvalue
                                % faculty.getStudent(rindex).getEmailContacts().length;
                        int posPhone = rvalue
                                % faculty.getStudent(rindex).getPhoneContacts().length;

                        System.out.println(faculty.getStudent(rindex)
                                .getEmailContacts()[posEmail].isNewerThan(faculty
                                .getStudent(rindex).getPhoneContacts()[posPhone]));
                    }

                    break;
                }

                case "PRINT_FACULTY_METHODS": {
                    System.out.println("Faculty: " + faculty.toString());
                    System.out.println("Student with most contacts: "
                            + faculty.getStudentWithMostContacts().toString());
                    break;
                }

            }

        }

        scanner.close();
    }
}
