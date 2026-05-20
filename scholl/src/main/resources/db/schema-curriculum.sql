-- Chạy script này trên database schoolmanager (SQL Server)
-- sau khi đã chạy schema-majors.sql (cần departments, majors).

-- ========== semesters ==========
IF OBJECT_ID(N'dbo.semesters', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.semesters (
        id            UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_semesters PRIMARY KEY DEFAULT NEWID(),
        code          NVARCHAR(100)    NOT NULL,
        name          NVARCHAR(255)    NOT NULL,
        academic_year NVARCHAR(20)     NULL
    );
    CREATE UNIQUE INDEX UX_semesters_code ON dbo.semesters (code);
END
GO

-- ========== training_programs ==========
IF OBJECT_ID(N'dbo.training_programs', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.training_programs (
        id                  UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_training_programs PRIMARY KEY DEFAULT NEWID(),
        code                NVARCHAR(100)    NOT NULL,
        name                NVARCHAR(255)    NOT NULL,
        name_en             NVARCHAR(255)    NULL,
        major_id            UNIQUEIDENTIFIER NOT NULL,
        department_id       UNIQUEIDENTIFIER NOT NULL,
        degree_level        NVARCHAR(50)     NULL,
        education_type      NVARCHAR(50)     NULL,
        total_credits       DECIMAL(5, 1)    NULL,
        required_credits    DECIMAL(5, 1)    NULL,
        elective_credits    DECIMAL(5, 1)    NULL,
        internship_credits  DECIMAL(5, 1)    NULL,
        thesis_credits      DECIMAL(5, 1)    NULL,
        admission_year      DATE             NULL,
        duration_years      DECIMAL(5, 1)    NULL,
        max_duration_years  DECIMAL(5, 1)    NULL,
        effective_date      DATE             NULL,
        expiry_date         DATE             NULL,
        description         NVARCHAR(MAX)    NULL,
        objectives          NVARCHAR(MAX)    NULL,
        learning_outcomes   NVARCHAR(MAX)    NULL,
        version             NVARCHAR(20)     NULL,
        status              NVARCHAR(20)     NULL,
        created_at          DATETIME2        NOT NULL CONSTRAINT DF_tp_created_at DEFAULT SYSUTCDATETIME(),
        updated_at          DATETIME2        NULL,
        created_by          NVARCHAR(255)    NULL,
        updated_by          NVARCHAR(255)    NULL,
        deleted_at          DATETIME2        NULL,
        deleted_by          NVARCHAR(255)    NULL,
        is_active           BIT              NOT NULL CONSTRAINT DF_tp_is_active DEFAULT (1),
        CONSTRAINT FK_tp_majors FOREIGN KEY (major_id) REFERENCES dbo.majors (id),
        CONSTRAINT FK_tp_departments FOREIGN KEY (department_id) REFERENCES dbo.departments (id)
    );
    CREATE INDEX IX_tp_major_id ON dbo.training_programs (major_id);
    CREATE INDEX IX_tp_department_id ON dbo.training_programs (department_id);
    CREATE UNIQUE INDEX UX_tp_code_active ON dbo.training_programs (code) WHERE deleted_at IS NULL;
END
GO

-- ========== courses ==========
IF OBJECT_ID(N'dbo.courses', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.courses (
        id                 UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_courses PRIMARY KEY DEFAULT NEWID(),
        department_id      UNIQUEIDENTIFIER NOT NULL,
        code               NVARCHAR(100)    NOT NULL,
        name               NVARCHAR(255)    NOT NULL,
        name_en            NVARCHAR(255)    NULL,
        credits            DECIMAL(5, 1)    NULL,
        course_type        VARCHAR(20)      NULL,
        theory_hours       DECIMAL(5, 1)    NULL,
        practice_hours     DECIMAL(5, 1)    NULL,
        self_study_hours   DECIMAL(5, 1)    NULL,
        internship_credits DECIMAL(5, 1)    NULL,
        description        NVARCHAR(MAX)    NULL,
        created_at         DATETIME2        NOT NULL CONSTRAINT DF_courses_created_at DEFAULT SYSUTCDATETIME(),
        updated_at         DATETIME2        NULL,
        created_by         NVARCHAR(255)    NULL,
        updated_by         NVARCHAR(255)    NULL,
        deleted_at         DATETIME2        NULL,
        deleted_by         NVARCHAR(255)    NULL,
        is_active          BIT              NOT NULL CONSTRAINT DF_courses_is_active DEFAULT (1),
        CONSTRAINT FK_courses_departments FOREIGN KEY (department_id) REFERENCES dbo.departments (id)
    );
    CREATE INDEX IX_courses_department_id ON dbo.courses (department_id);
    CREATE UNIQUE INDEX UX_courses_code_active ON dbo.courses (code) WHERE deleted_at IS NULL;
END
GO

-- ========== training_program_courses ==========
IF OBJECT_ID(N'dbo.training_program_courses', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.training_program_courses (
        id                       UNIQUEIDENTIFIER NOT NULL CONSTRAINT PK_tpc PRIMARY KEY DEFAULT NEWID(),
        training_program_id      UNIQUEIDENTIFIER NOT NULL,
        course_id                UNIQUEIDENTIFIER NOT NULL,
        course_code              NVARCHAR(100)    NULL,
        course_name              NVARCHAR(255)    NULL,
        semester_id              UNIQUEIDENTIFIER NULL,
        semester_code            NVARCHAR(100)    NULL,
        academic_year            NVARCHAR(20)     NULL,
        is_required              BIT              NOT NULL CONSTRAINT DF_tpc_is_required DEFAULT (1),
        group_code               NVARCHAR(50)     NULL,
        credits                  DECIMAL(5, 1)    NULL,
        prerequisite_course_id   UNIQUEIDENTIFIER NULL,
        is_prerequisite_required BIT              NOT NULL CONSTRAINT DF_tpc_prereq_req DEFAULT (0),
        note                     NVARCHAR(500)    NULL,
        sort_order               INT              NULL,
        status                   NVARCHAR(50)     NULL,
        created_at               DATETIME2        NOT NULL CONSTRAINT DF_tpc_created_at DEFAULT SYSUTCDATETIME(),
        updated_at               DATETIME2        NULL,
        created_by               NVARCHAR(255)    NULL,
        updated_by               NVARCHAR(255)    NULL,
        deleted_at               DATETIME2        NULL,
        deleted_by               NVARCHAR(255)    NULL,
        is_active                BIT              NOT NULL CONSTRAINT DF_tpc_is_active DEFAULT (1),
        CONSTRAINT FK_tpc_tp FOREIGN KEY (training_program_id) REFERENCES dbo.training_programs (id),
        CONSTRAINT FK_tpc_course FOREIGN KEY (course_id) REFERENCES dbo.courses (id),
        CONSTRAINT FK_tpc_semester FOREIGN KEY (semester_id) REFERENCES dbo.semesters (id),
        CONSTRAINT FK_tpc_prereq FOREIGN KEY (prerequisite_course_id) REFERENCES dbo.courses (id)
    );
    CREATE INDEX IX_tpc_training_program_id ON dbo.training_program_courses (training_program_id);
    CREATE INDEX IX_tpc_course_id ON dbo.training_program_courses (course_id);
    CREATE INDEX IX_tpc_semester_id ON dbo.training_program_courses (semester_id);
    CREATE UNIQUE INDEX UX_tpc_program_course_active
        ON dbo.training_program_courses (training_program_id, course_id) WHERE deleted_at IS NULL;
END
GO

-- Dữ liệu mẫu
IF NOT EXISTS (SELECT 1 FROM dbo.majors WHERE code = N'CNTT')
BEGIN
    INSERT INTO dbo.majors (id, department_id, code, name, is_active)
    VALUES (
        '22222222-2222-2222-2222-222222222201',
        '11111111-1111-1111-1111-111111111101',
        N'CNTT',
        N'Công nghệ thông tin',
        1
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.semesters)
BEGIN
    INSERT INTO dbo.semesters (id, code, name, academic_year) VALUES
        ('33333333-3333-3333-3333-333333333301', N'HK1-2024', N'Học kỳ 1 năm 2024', N'2024-2025'),
        ('33333333-3333-3333-3333-333333333302', N'HK2-2024', N'Học kỳ 2 năm 2024', N'2024-2025');
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.training_programs)
BEGIN
    INSERT INTO dbo.training_programs (
        id, code, name, name_en, major_id, department_id,
        degree_level, education_type, total_credits, required_credits,
        admission_year, duration_years, status, version, created_by
    ) VALUES (
        '44444444-4444-4444-4444-444444444401',
        N'CTDT-CNTT-2024',
        N'Chương trình CNTT 2024',
        N'IT Program 2024',
        '22222222-2222-2222-2222-222222222201',
        '11111111-1111-1111-1111-111111111101',
        N'Đại học', N'Chính quy', 120.0, 110.0,
        '2024-09-01', 4.0, N'ACTIVE', N'1.0', N'system'
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.courses)
BEGIN
    INSERT INTO dbo.courses (
        id, department_id, code, name, credits, course_type,
        theory_hours, practice_hours, created_by
    ) VALUES
        ('55555555-5555-5555-5555-555555555501', '11111111-1111-1111-1111-111111111101',
         N'CS101', N'Nhập môn lập trình', 3.0, N'REQUIRED', 30.0, 15.0, N'system'),
        ('55555555-5555-5555-5555-555555555502', '11111111-1111-1111-1111-111111111101',
         N'CS102', N'Cấu trúc dữ liệu', 3.0, N'REQUIRED', 30.0, 15.0, N'system');
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.training_program_courses)
BEGIN
    INSERT INTO dbo.training_program_courses (
        id, training_program_id, course_id, course_code, course_name,
        semester_id, semester_code, academic_year, is_required, credits, sort_order, status, created_by
    ) VALUES (
        '66666666-6666-6666-6666-666666666601',
        '44444444-4444-4444-4444-444444444401',
        '55555555-5555-5555-5555-555555555501',
        N'CS101', N'Nhập môn lập trình',
        '33333333-3333-3333-3333-333333333301', N'HK1-2024', N'2024-2025',
        1, 3.0, 1, N'ACTIVE', N'system'
    );
END
GO
