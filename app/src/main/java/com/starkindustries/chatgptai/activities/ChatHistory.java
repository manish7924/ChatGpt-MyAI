package com.starkindustries.chatgptai.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.starkindustries.chatgptai.MainActivity;
import com.starkindustries.chatgptai.R;
import com.starkindustries.chatgptai.chatmodel.Message;
import com.starkindustries.chatgptai.chatmodel.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
public class ChatHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        // Retrieve sent messages
        List<Message> sentMessages = new ArrayList<>();
        sentMessages.add(new Message("What is AI ?", Message.SEND_BY_ME));
        sentMessages.add(new Message("AI, or artificial intelligence, refers to the development of computer systems that can perform tasks that typically require human intelligence. These tasks include learning, reasoning, problem-solving, understanding natural language, speech recognition, and visual perception. AI aims to create machines or algorithms that can mimic human cognitive functions and adapt to new information or stimuli.\n" +
                "\n" +
                "There are several types of AI, including:\n" +
                "\n" +
                "Narrow or Weak AI (ANI):\n" +
                "Narrow AI is designed to perform a specific task or a set of closely related tasks. It excels within its defined domain, but it lacks the ability to generalize its understanding or perform tasks outside of its expertise. Examples include virtual personal assistants like Siri or Alexa.\n" +
                "\n" +
                "Artificial General Intelligence (AGI):\n" +
                "AGI refers to AI that possesses human-level intelligence and can understand, learn, and apply knowledge across a wide range of tasks just like a human being. AGI is a hypothetical form of AI that does not currently exist but is a topic of theoretical discussion.\n" +
                "\n" +
                "Superintelligence:\n" +
                "Superintelligence is a level of AI that surpasses human intelligence in all aspects, including creativity, problem-solving, and overall cognitive abilities. This is also a theoretical concept and has not been achieved.", Message.SEND_BY_BOT));

        sentMessages.add(new Message("How to make Maggie ?", Message.SEND_BY_ME));
        sentMessages.add(new Message("To make Maggi, a popular brand of instant noodles, follow these basic steps:\n" +
                "\n" +
                "Ingredients:\n" +
                "\n" +
                "1 packet of Maggi noodles\n" +
                "Water\n" +
                "Maggi masala seasoning (usually included in the packet)\n" +
                "Optional: Vegetables, such as peas, carrots, and bell peppers\n" +
                "Instructions:\n" +
                "\n" +
                "Boil Water: Fill a pot with enough water to cook the noodles. Bring it to a boil.\n" +
                "\n" +
                "Break Noodles: Open the Maggi packet and break the noodles into smaller pieces. You can do this while the water is heating.\n" +
                "\n" +
                "Add Noodles: Once the water is boiling, add the broken Maggi noodles to the pot.\n" +
                "\n" +
                "Cook Noodles: Cook the noodles for about 2-3 minutes until they become soft. You can stir occasionally to prevent sticking.\n" +
                "\n" +
                "Drain Water: If you prefer your Maggi dry, drain the water using a strainer. If you want it soupy, you can leave some water in the pot.\n" +
                "\n" +
                "Add Seasoning: Sprinkle the Maggi masala seasoning that comes with the packet onto the cooked noodles. You can also add any optional vegetables at this stage.\n" +
                "\n" +
                "Mix and Serve: Stir the seasoning and vegetables into the noodles until well combined. Your Maggi is ready to be served.\n" +
                "\n" +
                "Enjoy your homemade Maggi! You can customize it with your favorite ingredients and spices to suit your taste.", Message.SEND_BY_BOT));


        sentMessages.add(new Message("How to learn DSA ? Provide me \nresource to learn DSA.", Message.SEND_BY_ME));
        sentMessages.add(new Message("Certainly, here are some valuable resources for learning Data Structures and Algorithms (DSA):\n" +
                "\n" +
                "Choose a Free Programming Language:\n" +
                "Start with a programming language that is freely available and well-documented. Python, JavaScript, and Ruby are good options.\n" +
                "\n" +
                "Online Tutorials and Courses:\n" +
                "Numerous websites offer free programming tutorials and courses. Some popular platforms include:\n" +
                "   - Codecademy\n" +
                "   - Coursera (audit courses for free)\n" +
                "   - EdX - Harvard University Courses (audit courses for free by David J. Malan)\n" +
                "   - Khan Academy\n" +
                "   - MIT OpenCourseWare\n" +
                "   - Harvard's CS50 on edX\n" +
                "   - Mozilla Developer Network (for web development)\n" +
                "   - CodeWithHarry (focused on web development by Haris Ali Khan)\n" +
                "\n" +
                "YouTube Tutorials:\n" +
                "YouTube is a goldmine of free programming tutorials. Many experienced developers and educators offer in-depth lessons on a wide range of programming topics. Channels like \"The Net Ninja,\" \"Traversy Media,\" and \"Academind\" are highly recommended.\n" +
                "\n" +
                "Books and Ebooks:\n" +
                "Project Gutenberg and other free ebook sources often have programming books available. Websites like \"Automate the Boring Stuff with Python\" offer free programming books online.\n" +
                "\n" +
                "Official Documentation:\n" +
                "The official documentation of your chosen programming language is an invaluable resource. It's comprehensive and free.\n" +
                "\n" +
                "Coding Platforms:\n" +
                "Sign up for free accounts on coding platforms like GitHub, GitLab, and Bitbucket to practice version control and collaborate on open-source projects.\n" +
                "\n" +
                "Online Coding Challenges and Competitions:\n" +
                "Websites like LeetCode, HackerRank, and Codeforces offer a plethora of coding challenges for free. You can use these platforms to practice and improve your problem-solving skills.\n" +
                "\n" +
                "Open Source Projects:\n" +
                "Contribute to open-source projects on GitHub to gain practical experience and collaborate with experienced developers.\n" +
                "\n" +
                "Forums and Communities:\n" +
                "Join online communities like Stack Overflow, Reddit's programming subreddits, and Discord servers focused on coding. These platforms are great for asking questions and learning from others.\n" +
                "\n" +
                "Project-Based Learning:\n" +
                "Create personal projects to apply what you've learned. For web development, build a personal website or a simple web app. For other fields, create tools or applications that interest you.\n" +
                "\n" +
                "Podcasts and Blogs:\n" +
                "Listen to programming-related podcasts and follow blogs for insightful discussions, interviews, and articles. Some popular programming podcasts are \"Coding Blocks\" and \"Software Engineering Daily.\"\n" +
                "\n" +
                "Educational Institutions:\n" +
                "Many universities and institutions offer free course materials and lecture recordings. Search for specific courses you're interested in, such as \"Introduction to Computer Science\" from MIT.\n" +
                "\n" +
                "Programming Forums and Q&A Sites:\n" +
                "Utilize forums like Stack Overflow, where you can ask questions, find answers, and learn from others' code.\n" +
                "\n" +
                "Programming Challenges and Hackathons:\n" +
                "Participate in local or online hackathons and coding challenges to gain practical experience and collaborate with others.\n" +
                "\n" +
                "Online Coding Playgrounds:\n" +
                "Websites like Repl.it and CodePen allow you to write and run code in the browser, making it easy to experiment and share your code.\n" +
                "\n" +
                "Remember that consistency and practice are key to learning programming effectively. While free resources are abundant, it's important to set goals, work on projects, and continually challenge yourself to become a proficient programmer.", Message.SEND_BY_BOT));

//        sentMessages.add(new Message("How to learn Java programming Language ?", Message.SEND_BY_ME));
//        sentMessages.add(new Message("omputer", Message.SEND_BY_BOT));

        // Set up RecyclerView to display sent messages
        RecyclerView recyclerView = findViewById(R.id.chat_history_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Create and set the adapter
        MessageAdapter messageAdapter = new MessageAdapter(sentMessages);
        recyclerView.setAdapter(messageAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void btnBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        super.onBackPressed();
        finish();
    }
}
