package com.journal.journal_service.constants;

public class AppConstants {

    public static final String WEEKLY = "Weekly";
    public static final String MONTHLY = "Monthly";
    public static final String DAILY = "Daily";


    public static final Integer WEEKLY_DAYS = 7;
    public static final Integer MONTHLY_DAYS = 30;
    public static final Integer DAILY_DAY = 1;


    public static final String MAIL_SUBJECT_PREFIX = "JournalList [REMINDER]: ";

//    public static final String OPEN_AI_PROMPT_FOR_JOB_DESCRIPTION = "\n\n Based on the above job description give me a few points to make my resume more impactful, send your response only in a structured html string without any \\n or escape characters, dont send anything else";

    public static final String OPEN_AI_PROMPT_FOR_JOB_DESCRIPTION = "\n\n Based on the above job description give me a few points to make my resume more impactful, send your response only so that this response can be structured into a pdf file, dont send anything else";

    public static final String OPEN_AI_PROMPT_FOR_JOB_DESCRIPTION_RESUME = "\n\n Based on the above resume and job description analyze how well the following resume matches this job description, give me a full in depth analysis what i can do to improve the resume and make it more impactful, send your response only so that this response can be structured into a pdf file, dont send anything else";



    public static final String FOLDER_PREFIX_PATH = "resumes/";
    public static final String FOLDER_PREFIX_PATH_GENERATED = "generated/";

    public static final String QUEUED_REPORT = "Queued";
    public static final String IN_PROGRESS_REPORT = "Processing";
    public static final String GENERATED_REPORT = "Generated";
    public static final String FAILED_REPORT = "Failed";
}
