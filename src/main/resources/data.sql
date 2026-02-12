-- Reset tables in correct FK order
TRUNCATE TABLE reports CASCADE;
TRUNCATE TABLE report_specs CASCADE;
TRUNCATE TABLE cylinders CASCADE;
TRUNCATE TABLE tape_specs CASCADE;
TRUNCATE TABLE notes CASCADE;
TRUNCATE TABLE jobs CASCADE;

-- ===========================
-- INSERT JOBS
-- ===========================
INSERT INTO jobs (job_date, job_number, job_name, info, cylinder_circumference, cylinder_width)
VALUES
    ('2026-01-10 10:00:00', 'J-1001', 'Job Alpha', 'Demo job 1', 850, 1300),
    ('2026-01-15 11:30:00', 'J-1002', 'Job Bravo', 'Demo job 2', 900, 1400),
    ('2026-01-20 09:45:00', 'J-1003', 'Job Charlie', 'Demo job 3', 1000, 1500);

-- JOB IDs now are:
-- 1 = Alpha
-- 2 = Bravo
-- 3 = Charlie


-- ===========================
-- INSERT NOTES (NULL LOB FIELD!)
-- ===========================
INSERT INTO notes (job_id, info) VALUES
                                     (1, NULL),
                                     (2, NULL),
                                     (3, NULL);


-- ===========================
-- INSERT TAPE SPECS
-- ===========================
INSERT INTO tape_specs (tape_name, tape_type, thickness, info)
VALUES
    ('TapeSoft 10', 'Soft', 10, 'Soft tape for fine print'),
    ('TapeSolid 15', 'Solid', 15, 'General purpose tape'),
    ('TapePro 20', 'Pro', 20, 'High durability');


-- IDs:
-- 1 = TapeSoft 10
-- 2 = TapeSolid 15
-- 3 = TapePro 20


-- ===========================
-- INSERT REPORT SPECS
-- ===========================
INSERT INTO report_specs (report_name, report_type, thickness, info)
VALUES
    ('Spec A', 'Type A', 300, 'High coverage'),
    ('Spec B', 'Type B', 250, 'Standard coverage'),
    ('Spec C', 'Type C', 200, 'Fine detail');


-- IDs:
-- 1 = Spec A
-- 2 = Spec B
-- 3 = Spec C


-- ===========================
-- INSERT CYLINDERS
-- ===========================
INSERT INTO cylinders (job_id, cylinder_nr, tape_spec_id, color, cylinder_info)
VALUES
    -- Job 1 (Alpha) → 2 cylinders
    (1, 1, 1, 'Cyan', 'Alpha – cylinder 1'),
    (1, 2, 2, 'Magenta', 'Alpha – cylinder 2'),

    -- Job 2 (Bravo) → 1 cylinder
    (2, 1, 2, 'Yellow', 'Bravo – cylinder 1'),

    -- Job 3 (Charlie) → 2 cylinders
    (3, 1, 3, 'Black', 'Charlie – cylinder 1'),
    (3, 2, 1, 'Green', 'Charlie – cylinder 2');


-- Cylinder IDs now:
-- Job 1: 1, 2
-- Job 2: 3
-- Job 3: 4, 5


-- ===========================
-- INSERT REPORTS
-- ===========================
INSERT INTO reports (cylinder_id, report_nr, report_width, x_offset, y_offset, report_spec_id)
VALUES
    -- Job 1 - Cylinder 1
    (1, 1, 300, 10, 20, 1),
    (1, 2, 320, 15, 25, 2),

    -- Job 1 - Cylinder 2
    (2, 1, 280, 5, 10, 3),

    -- Job 2 - Cylinder 1
    (3, 1, 310, 8, 18, 2),
    (3, 2, 330, 12, 22, 1),

    -- Job 3 - Cylinder 1
    (4, 1, 350, 20, 30, 3),

    -- Job 3 - Cylinder 2
    (5, 1, 360, 25, 35, 1),
    (5, 2, 370, 30, 40, 2);
