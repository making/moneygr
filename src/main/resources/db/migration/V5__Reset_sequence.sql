SELECT setval('parent_outcome_category_parent_outcome_category_id_seq', (SELECT MAX (parent_outcome_category_id) FROM parent_outcome_category));
SELECT setval('daily_outcome_category_daily_outcome_category_id_seq', (SELECT MAX (daily_outcome_category_id) FROM daily_outcome_category));
SELECT setval('income_category_income_category_id_seq', (SELECT MAX (income_category_id) FROM income_category));
SELECT setval('family_family_id_seq', (SELECT MAX (family_id) FROM family));
SELECT setval('user_user_id_seq', (SELECT MAX (user_id) FROM "user"));